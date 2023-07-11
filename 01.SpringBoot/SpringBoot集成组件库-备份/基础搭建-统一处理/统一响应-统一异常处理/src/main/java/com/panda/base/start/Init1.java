package com.panda.base.start;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Init1 {

    @PostConstruct
    public void init() {
        System.out.println("init1");
    }

}
