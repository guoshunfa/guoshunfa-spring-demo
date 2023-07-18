package com.guoshunfa.springboot3quartzdemo.test;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            System.out.println("我是HelloJob，我开始执行。");
            Thread.sleep(600);
            System.out.println("我是HelloJob，我执行结束。");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
