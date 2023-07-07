package com.panda.kafka;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description kafka 消费位置配置
 **/
public enum KafkaConsumptionLocationConfig {
    // 最新的数据开始消费
    NEW("largest"),
    // 最开始消费数据
    FIRST("smallest");

    @Getter @Setter
    private String value;

    KafkaConsumptionLocationConfig(String value) {
        this.value = value;
    }
}
