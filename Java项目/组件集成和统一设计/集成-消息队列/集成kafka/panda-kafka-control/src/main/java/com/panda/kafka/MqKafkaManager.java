package com.panda.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Properties;

/**
 * kafka消息发送组件
 **/
@Slf4j
@Component
public class MqKafkaManager {

    @Value("${kafka.bootstrap.servers}")
    String kafkaAddr;

    @Value("${kafka.enable:false}")
    Boolean kafkaEnable;

    public static String gentKey(String clazz) {
        KafkaKey kafkaKey = new KafkaKey();
        kafkaKey.setClassType(clazz);
        return JSON.toJSONString(kafkaKey);
    }

    public static String genValue(Serializable value) {
        return JSON.toJSONString(value, SerializerFeature.WriteClassName, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 发送数据到kafka
     *
     * @param topic
     * @param value 要发送的数据
     */
    public boolean send(String topic, Serializable value) {
        try {
            String t = topic;
            String k = gentKey(value.getClass().getTypeName());
            String v = genValue(value);
            final ProducerRecord<String, String> record = new ProducerRecord<>(t, k, v);

            Properties kafkaPropertie = new Properties();
            kafkaPropertie.put("bootstrap.servers", kafkaAddr);
            kafkaPropertie.put("key.serializer", StringSerializer.class.getTypeName());
            kafkaPropertie.put("value.serializer", StringSerializer.class.getTypeName());

            RecordMetadata metadata = this.kafkaProducer().send(record).get();
            if (!v.contains("\"@type\":\"com.bfhl.base.protocol.ForwardVO\"")) {//转发的不打印
                log.debug("partition={},topic={},offset={}, msg:{}", metadata.partition(), metadata.topic(), metadata.offset(), v);
            }
            return true;
        } catch (Exception e) {
            log.error("kafka发送失败", e);
            return false;
        }
    }

    private KafkaProducer<String, String> kafkaProducer() {
        if (!kafkaEnable) {
            return null;
        }
        Properties kafkaPropertie = new Properties();
        kafkaPropertie.put("bootstrap.servers", kafkaAddr);
        kafkaPropertie.put("key.serializer", StringSerializer.class.getTypeName());
        kafkaPropertie.put("value.serializer", StringSerializer.class.getTypeName());
        return new KafkaProducer(kafkaPropertie);
    }

}
