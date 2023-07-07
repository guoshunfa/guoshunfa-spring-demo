package com.panda.kafka;

import lombok.Data;

/**
 * 每个监听方法一个，要做重复验证
 **/
@Data
public class KafkaConfig {

    /**
     * Kafka Topic名称
     */
    private String topic;

    /**
     * groupId
     */
    private String groupId;

    /**
     * 线程数量，一般就是Topic的分区数量
     */
    private int numThreads;

    /**
     * zookeeper连接地址，如："cdrmwan.hopto.org:2089,cdrmwan.hopto.org:2029,cdrmwan.hopto.org:2059"
     */
    private String zkAddrs;

    /**
     * zookeeper session时间（毫秒）
     */
    private Integer zkSessionTimeoutMs;

    /**
     * zookeeper 同步时间（毫秒）
     */
    private Integer zkSyncTimeMs;


    /**
     * zookeeper 自动提交时间（毫秒）
     */
    private Integer autoCommitIntervalMs;


    /**
     * 偏移方式
     */
    private String autoOffsetReset;
}
