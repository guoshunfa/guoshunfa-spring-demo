package com.panda.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PandaBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PandaBaseApplication.class, args);
        System.out.println("init4");
    }

}
