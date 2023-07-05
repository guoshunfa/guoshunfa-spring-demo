package com.panda.listener;

import kafka.message.MessageAndMetadata;

/**
 * 消息消费接口
 */
public interface IMessageConsumer {

    /**
     * 消息消费
     *
     * @param metadata 接收到的元数据
     */
    void consume(MessageAndMetadata metadata);

}
