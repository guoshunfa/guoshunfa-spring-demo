package com.panda.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName Test01Listener
 * @Description 请描述类的业务用途
 * @Author guoshunfa
 * @Date 2022/1/12 5:47 PM
 * @Version 1.0
 **/
@Component
public class Test01Listener {

    @KafkaListener(topics = {"test01"})
    public void test01(Object message) {
        System.out.println(message);
    }

}
