package com.panda.kafka;

import com.panda.listener.IMessageConsumer;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @Description 自定义kafka消费者
 **/
@SuppressWarnings("ALL")
@Slf4j
public class KafkaConsumer extends Thread {


    /**
     * 线程编号
     */
    private final AtomicInteger threadNo = new AtomicInteger();

    /**
     * Kafka数据消费对象
     */
    private ConsumerConnector consumer;

    /**
     * Kafka Topic名称
     */
    private String topic;

    private String groupId;
    /**
     * 线程数量，一般就是Topic的分区数量
     */
    private int numThreads;

    /**
     * 线程池
     */
    private ExecutorService executorPool;


    /**
     * 消费接口
     */
    private IMessageConsumer consumerCallback;

    /**
     * 每个@MqTopicGroupListener对应一个，topic可能重复
     * 构造函数
     *
     * @param topic      Kafka消息Topic主题
     * @param numThreads 处理数据的线程数/可以理解为Topic的分区数
     */
    public KafkaConsumer(KafkaConfig config, IMessageConsumer consumerCallback) {
        // 创建Kafka连接器
        this.consumer = Consumer.createJavaConsumerConnector(createConsumerConfig(config, config.getGroupId()));
        this.topic = config.getTopic();
        this.groupId = config.getGroupId();
        this.numThreads = config.getNumThreads();
        this.consumerCallback = consumerCallback;
    }

    @Override
    public void run() {
        // 指定Topic
        Map<String, Integer> topicCountMap = new HashMap<>(20);
        topicCountMap.put(this.topic, this.numThreads);//一个group只能消费一个topic，消费者线程数

        // 指定数据的解码器
        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

        // 获取连接数据的迭代器对象集合
        Map<String, List<KafkaStream<String, String>>> consumerMap = this.consumer.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);

        // 从返回结果中获取对应topic的数据流处理器
        List<KafkaStream<String, String>> streams = consumerMap.get(this.topic);
        log.debug("gruop listener topic:{},groupId:{},thread.size:{}", this.topic, this.groupId, consumerMap.values().stream().flatMap(e -> e.stream()).count());
        this.executorPool = newFixedThreadPool(this.numThreads, this.topic);
        for (final KafkaStream<String, String> stream : streams) {
            this.executorPool.submit(new ConsumerKafkaStreamProcesser(stream, topic, this.groupId, this.consumerCallback));
        }
    }


    private ExecutorService newFixedThreadPool(int numThreads, String topic) {

        ThreadFactory factory = r -> {
            Thread t = new Thread(r);
            t.setName("kafkaTopic_" + topic + "_" + threadNo.incrementAndGet());
            return t;
        };

        return new ThreadPoolExecutor(numThreads, numThreads, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), factory, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void shutdown() {
        if (this.consumer != null) {
            this.consumer.shutdown();
        }
        if (this.executorPool != null) {
            this.executorPool.shutdown();
            try {
                final int timeout = 1;
                if (!this.executorPool.awaitTermination(timeout, TimeUnit.SECONDS)) {
                    log.debug("线程停止中....");
                }
            } catch (InterruptedException e) {
                log.error("线程池停止终端", e);
            }
        }
    }

    /**
     * 根据传入的zk的连接信息和groupID的值创建对应的ConsumerConfig对象
     *
     * @param zookeeperConnect zk的连接信息，如： node1:2181,node2:2181,node3:2181
     * @param groupId          该kafka consumer所属的group id的值
     * @return Kafka连接信息
     */
    private ConsumerConfig createConsumerConfig(KafkaConfig config, String groupId) {

        Properties prop = new Properties();
        prop.put("group.id", groupId);
        prop.put("zookeeper.connect", config.getZkAddrs());
        prop.put("zookeeper.session.timeout.ms", config.getZkSessionTimeoutMs().toString());
        prop.put("zookeeper.sync.time.ms", config.getZkSyncTimeMs().toString());
        prop.put("auto.commit.interval.ms", config.getAutoCommitIntervalMs().toString());
        prop.put("auto.offset.reset", config.getAutoOffsetReset());
        return new ConsumerConfig(prop);
    }


    /**
     * Kafka消费者数据处理线程
     */
    public static class ConsumerKafkaStreamProcesser implements Runnable {

        private KafkaStream<String, String> stream;
        private String topic;
        private String groupId;
        private IMessageConsumer consumerCallback;

        public ConsumerKafkaStreamProcesser(KafkaStream<String, String> stream, String topic, String groupId, IMessageConsumer consumerCallback) {
            this.stream = stream;
            this.topic = topic;
            this.groupId = groupId;
            this.consumerCallback = consumerCallback;
        }

        /**
         * 剩余可用内存计算，单位M。
         */
        private long surplusMemory() {
            return Runtime.getRuntime().maxMemory() / 1024 / 1024 - Runtime.getRuntime().totalMemory() / 1024 / 1024;
        }

        @Override
        public void run() {
            //每个线程执行一次
            log.debug("ConsumerKafkaStreamProcesser.run  topic:{},groupId:{}", this.topic, this.groupId);
            ConsumerIterator<String, String> iter = this.stream.iterator();//流会持续接收消息
            while (iter.hasNext()) {// 阻塞直到有消息

                MessageAndMetadata message = iter.next();
                try {
                    //log.debug("ConsumerKafkaStreamProcesser.consume  topic:{},groupId:{}",this.topic,this.groupId);
                    consumerCallback.consume(message);
                } catch (Exception e) {
                    log.error("消费数据异常", e);
                }
            }
            log.debug("ConsumerKafkaStreamProcesser.run end topic:" + this.topic);
        }
    }
}

