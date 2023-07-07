package com.panda.web;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import com.panda.kafka.MqKafkaManager;
import com.panda.listener.MqTopicGroupListener;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class KafkaTestCTR {

    public final static String topic = "a_bc";
    public final static String topic2 = "a_bcd";
    public final static String topic3 = "prjTest";

    @Autowired
    private MqKafkaManager mqKafkaManager;

    @ApiOperation(value = "kafka测试", notes = "kakfa测试")
    @GetMapping("/kafka/test/send")
    public boolean send(String msg) {
        if (msg == null) {
            msg = System.currentTimeMillis() + "";
        }

        VoChild v = new VoChild();
        v.setNn("n" + msg);
        v.setAbc(msg);
        mqKafkaManager.send(KafkaTestCTR.topic, v);
        mqKafkaManager.send(KafkaTestCTR.topic2, ListUtil.toList(v));

        return true;
    }

    @MqTopicGroupListener(topic = KafkaTestCTR.topic)
    public void recived(VoParent msg) {
        System.out.println("收到信息" + JSON.toJSONString(msg));
        log.debug("kafka测试-1-收到信息:{}", JSON.toJSONString(msg));
    }


    // 测试List作为接受是否兼容
    @MqTopicGroupListener(topic = KafkaTestCTR.topic2)
    public void recived7(List<VoParent> msg) {
        System.out.println("收到信息7" + JSON.toJSONString(msg));
        log.debug("kafka测试-7-收到信息:{}", JSON.toJSONString(msg));
    }

}
