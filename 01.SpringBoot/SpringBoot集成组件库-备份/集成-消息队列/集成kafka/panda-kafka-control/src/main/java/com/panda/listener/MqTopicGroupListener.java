package com.panda.listener;

import com.panda.kafka.KafkaConsumptionLocationConfig;
import com.panda.kafka.KafkaServiceType;

import java.lang.annotation.*;

/**
 * 监听消息队列里面的数据。当指定groupId时，一个组下所有应用实例中只能有一个实例能消费到数据。当没有指定groupId时，没个实例都会被消费。
 * 同组下一个实例消费使用场景举例：“硬件数据保存” 服务启动了3个应用实例，我们为了保证数据不重复保存，那么我们需要指定groupId，如：@MqTopicGroupListener(topic="camera_data",groupId="save_camera_data");
 * 需要被每个实例消费使用场景举例：通知每个实例将自己内存当中的权限数据刷新，那么我们不需要指定groupId,如：@MqTopicGroupListener(topic="refresh_data");
 * <p>
 * 已兼容：
 * 1. 发送对象只要继承java.io.Serializable，都可以进行发送。
 * 2. 去掉groupId,通过类全名+方法+参数来决定groupId，全部实例都消费的通过属性定义。
 * 3. 可以通过配置，指定从哪里开始消费数据（支持最新和全部）。
 * 4. 可以指定使用哪个kafka服务，通过KAFKA_SERVICE_TYPE属性。
 * 5. 项目上下文设置，在框架内部设置，避免使用端忘记设置。
 * 遗留问题：
 * 1、指定消费线程数
 *
 *
 * @author guoshunfa
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MqTopicGroupListener {

    /**
     * TopicName<br/>
     * 1. 已支持spel表达式，通过属性topicIsUseSpel进行控制开关。（为了实现动态参数。）
     *
     * @return topic
     */
    String topic();

    /**
     * topic 是否使用了spel表达式
     *
     * @return topic 是否使用了spel表达式
     */
    boolean topicIsUseSpel() default false;

    /**
     * 消费组id，消费组的概率与kafka消费组的概念一致，当没有指定值时，每个应用实例都会消费同一条数据<br/>
     * 已弃用，不再继续维护。
     *
     * @return 消费组id
     */
    @Deprecated
    String groupId() default "";

    /**
     * kafka服务类型，默认为业务平台kafka。
     *
     * @return kafka服务类型
     */
    KafkaServiceType kafkaServiceType() default KafkaServiceType.COMMON;

    /**
     * 是否全部实例都执行，默认为否。
     *
     * @return
     */
    boolean isAllInstanceExecute() default false;

    /**
     * kafka 消费位置配置。默认为从最新开始消费。
     *
     * @return
     */
    KafkaConsumptionLocationConfig consumptionLocationConfig() default KafkaConsumptionLocationConfig.NEW;

}
