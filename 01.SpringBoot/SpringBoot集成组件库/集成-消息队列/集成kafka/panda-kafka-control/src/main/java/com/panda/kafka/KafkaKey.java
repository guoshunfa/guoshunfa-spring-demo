package com.panda.kafka;

import lombok.Data;

/**
 * @Description kafka 消息key
 **/
@Data
public class KafkaKey {

    /**
     * 数据类型，一般是数据对象的class的完整路径
     */
    private String classType;

}
