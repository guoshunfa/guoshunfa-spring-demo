package com.panda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description 请描述类的业务用途
 * @Author guoshunfa
 * @Date 2022/1/12 5:44 PM
 * @Version 1.0
 **/
@RestController
public class TestController {

    @Autowired
    public KafkaTemplate kafkaTemplate;

    @GetMapping("/send")
    public void sendMassage(){
        kafkaTemplate.send("test01", System.currentTimeMillis() + "");
    }
}
