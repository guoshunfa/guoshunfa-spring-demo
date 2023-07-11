package com.panda.base.start;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Init3 implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("init3");
    }
}
