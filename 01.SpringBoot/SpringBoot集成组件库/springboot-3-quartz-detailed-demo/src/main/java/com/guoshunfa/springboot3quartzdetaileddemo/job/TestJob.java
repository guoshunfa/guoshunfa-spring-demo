package com.guoshunfa.springboot3quartzdetaileddemo.job;

import org.springframework.stereotype.Service;

/**
 * 测试job
 **/
@Service
public class TestJob {

    public void doJob() {
        System.out.println("TestJob 执行" + System.currentTimeMillis());
    }
}
