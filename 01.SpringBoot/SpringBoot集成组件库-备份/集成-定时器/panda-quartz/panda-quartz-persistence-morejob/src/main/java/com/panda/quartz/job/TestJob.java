package com.panda.quartz.job;

import org.springframework.stereotype.Service;

/**
 * @ClassName TestJob
 * @Description 请描述类的业务用途
 * @Author guoshunfa
 * @Date 2021/11/19 下午2:39
 * @Version 1.0
 **/
@Service
public class TestJob {

    public void doJob() {
        System.out.println("TestJob 执行" + System.currentTimeMillis());
    }
}
