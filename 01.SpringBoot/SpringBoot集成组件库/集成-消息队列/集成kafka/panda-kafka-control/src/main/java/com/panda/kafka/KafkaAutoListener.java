package com.panda.kafka;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.panda.listener.ClassMessageConsumer;
import com.panda.listener.MqTopicGroupListener;
import com.panda.utils.SpelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 自动添加kafka的监听
 **/
@Slf4j
@Component
@Order(10000)
public class KafkaAutoListener implements ApplicationRunner {

    @Value("${kafka.enable:false}")
    protected Boolean kafkaEnable;

    @Value("${kafka.zookeeper.addrs}")
    protected String zkAddrs;

    @Value("${kafka.numThreads:3}")
    protected Integer kafkaNumThreads;

    @Value("${kafka.group.name.nospecial:true}")//group是否有特殊字符,比如_
    protected Boolean groupNameNospecial;

    @Value("${kafka.zookeeper.session.timeout.ms:20000}")
    protected Integer zkSessionTimeoutMs;

    @Value("${kafka.zookeeper.sync.time.ms:1000}")
    protected Integer zkSyncTimeMs;

    @Value("${kafka.auto.commit.interval.ms:1000}")
    protected Integer zkAutoCommitIntervalMs;

    @Autowired
    protected ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        autoCreateKafkaListener();
    }

    /**
     * 自动将com.bfhl 包下面注解了 @MqTopicGroupListener 的方法添加监听
     */
    public void autoCreateKafkaListener() {
        if (!kafkaEnable) {
            return;
        }
        String[] beans = applicationContext.getBeanDefinitionNames();
        for (String beanName : beans) {
            Class<?> beanType = applicationContext.getType(beanName);
            if (!StrUtil.startWith(beanType.getTypeName(), "com.panda")) {
                continue;
            }
            Object obj = applicationContext.getBean(beanName);
            addListener(obj);
        }
    }

    protected void addListener(Object obj) {
        Method[] ms = obj.getClass().getMethods();
        for (Method method : ms) {

            MqTopicGroupListener annotation = AnnotationUtils.findAnnotation(method, MqTopicGroupListener.class);
            if (null == annotation) {
                continue;
            }

            KafkaConsumer kafkaConsumer = null;
            if (kafkaEnable && KafkaServiceType.COMMON.equals(annotation.kafkaServiceType())) {
                kafkaConsumer = commonListener(annotation, obj, method);
            }
            log.info("kafka注册的消息：{}", JSONUtil.toJsonStr(annotation));

            if (kafkaConsumer != null) {
                kafkaConsumer.start();
            }
        }
    }

    private KafkaConsumer commonListener(MqTopicGroupListener annotation, Object obj, Method method) {
        KafkaConfig config = new KafkaConfig();

        String groupId = this.getGroupId(annotation.isAllInstanceExecute(), obj, method);

        if (StrUtil.isNotBlank(annotation.groupId())) {
            groupId = annotation.groupId() + method.getParameterTypes()[0].getTypeName();
        }
        config.setGroupId(groupId);

        config.setTopic(this.getTopic(annotation));
        config.setNumThreads(kafkaNumThreads);
        config.setZkAddrs(zkAddrs);
        config.setZkSessionTimeoutMs(this.zkSessionTimeoutMs);
        config.setAutoCommitIntervalMs(this.zkAutoCommitIntervalMs);
        config.setZkSyncTimeMs(this.zkSyncTimeMs);
        config.setAutoOffsetReset(annotation.consumptionLocationConfig().getValue());

        ClassMessageConsumer messageConsumer = new ClassMessageConsumer(method, obj);
        return new KafkaConsumer(config, messageConsumer);
    }

    private String getTopic(MqTopicGroupListener annotation) {
        // topic是否使用spel
        if (annotation.topicIsUseSpel()) {
            return SpelUtils.getValue(annotation.topic(), String.class);
        } else {
            return annotation.topic();
        }
    }

    private String getGroupId(boolean allInstanceExecute, Object obj, Method method) {
        String groupId;

        if (allInstanceExecute) {
            groupId = AopUtils.getTargetClass(obj).getSimpleName() + method.getName() + System.currentTimeMillis();
        } else {
            groupId = AopUtils.getTargetClass(obj).getSimpleName() + method.getName() + JSONObject.toJSONString(method.getParameterTypes());
        }
        // kafka不兼容的字符
        List<String> characters = Arrays.asList("", "[", "]", "$", "-", "_", "<", ">", "{", "}", "\"", ";");
        for (String character : characters) {
            groupId = groupId.replace(character, "");
        }
        return groupId;
    }

}
