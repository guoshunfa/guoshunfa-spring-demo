package com.panda.quartz.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.panda.quartz.entity.JobDataMap;
import org.junit.jupiter.api.Test;

/**
 * @ClassName JobControllerTest
 * @Description 请描述类的业务用途
 * @Author guoshunfa
 * @Date 2021/11/19 下午2:22
 * @Version 1.0
 **/
public class JobControllerTest {

    @Test
    public void saveJob() {
        String url = "http://127.0.0.1:9992/job/save";

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.setClassName("TestJob");
        jobDataMap.setJobName("测试job");
        jobDataMap.setCron("*/5 * * * * ?"); // 每隔5秒执行一次
        jobDataMap.setMethodName("doJob");
        jobDataMap.setPaused(true);

        String post = HttpUtil.post(url, JSONUtil.toJsonStr(jobDataMap));
        System.out.println(post);
    }

}
