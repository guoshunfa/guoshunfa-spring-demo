package com.panda.base.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ScheduledTest {

    @Scheduled(cron = "0/1 * * * * ?")
    public void test1() throws InterruptedException {
        System.out.println("test1: " + System.currentTimeMillis());
        TimeUnit.SECONDS.sleep(10);
    }

    @Scheduled(cron = "0/2 * * * * ?")
    public void test2() {
        System.out.println("test2: " + System.currentTimeMillis());
    }

}
