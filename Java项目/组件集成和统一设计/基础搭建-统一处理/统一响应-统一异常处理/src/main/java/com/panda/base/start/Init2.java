package com.panda.base.start;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class Init2 implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("init2");
    }
}
