package com.guoshunfa.springboot3quartzdetaileddemo.config;

import com.guoshunfa.springboot3quartzdetaileddemo.entity.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;

/**
 * job统一执行类
 */
//@DisallowConcurrentExecution // 并发处理
public class JobAutoExe extends QuartzJobBean {

    /**
     * 由quartz框架自动设值：jobDetail.getJobDataMap().put("jobId", bean.getId()) 这里面的值都会被设置到实例中
     */
    private String jobId;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = mongoTemplate.findById(jobId, JobDataMap.class);
        if (jobDataMap == null) {
            return;
        }
        try {
            Class<?> aClass = Class.forName(jobDataMap.getClassName());
            Object service = aClass.newInstance();
            Method method = service.getClass().getMethod(jobDataMap.getMethodName());
            method.invoke(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
