package com.panda.listener;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.panda.kafka.KafkaKey;
import kafka.message.MessageAndMetadata;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @Description 根据参数类型决定是否消费
 **/
@Slf4j
public class ClassMessageConsumer implements IMessageConsumer {

    /**
     * 回调的对象
     */
    private Object instance;

    /**
     * 收到数据后要回调的方法
     */
    private Method method;

    /**
     * 收到数据后要回调方法的参数，当参数能匹配上则回调
     */
    private Class<?>[] paramTypes;


    public ClassMessageConsumer(Method method, Object instance) {
        this.paramTypes = method.getParameterTypes();
        if (paramTypes.length != 1) {

            StringBuffer sb = new StringBuffer();
            sb.append(instance.getClass().getTypeName());
            sb.append(":").append(method.getName());
            sb.append("  必须有且只有一个参数");

            throw new IllegalArgumentException(sb.toString());
        }
        this.method = method;
        this.instance = instance;

    }

    @SneakyThrows
    @Override
    public void consume(MessageAndMetadata metadata) {
        log.debug("kafka consume" + metadata);
        if (StrUtil.isBlank((String) metadata.key())) {
            invoke(metadata);
            return;
        }

        KafkaKey k = JSON.parseObject(metadata.key().toString(), KafkaKey.class);

        Class paramClass = paramTypes[0];

        Class clazz = Class.forName(k.getClassType());

        if (!paramClass.isAssignableFrom(clazz)) {
            log.debug("kafka consume on match" + metadata);
            return;
        }
        invoke(metadata);
    }

    private void invoke(MessageAndMetadata metadata) {
        try {
            Object param = JSON.parseObject(metadata.message().toString(), paramTypes[0]);
            long start = System.currentTimeMillis();
            method.invoke(this.instance, param);
            long end = System.currentTimeMillis();
        } catch (Exception e) {
            log.error("消息消费出错", e);
        }

    }
}
