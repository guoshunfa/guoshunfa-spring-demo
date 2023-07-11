package com.panda.quartz.config;

import cn.hutool.extra.spring.SpringUtil;
import com.panda.quartz.entity.JobDataMap;
import lombok.Getter;
import lombok.Setter;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;

/**
 * @ClassName JobAutoExe
 * @Description job统一执行类
 * @Author guoshunfa
 * @Date 2021/7/10 下午10:53
 * @Version 1.0
 **/
//@DisallowConcurrentExecution // 并发处理
public class JobAutoExe extends QuartzJobBean {

    /**
     * 由quartz框架自动设值：jobDetail.getJobDataMap().put("jobId", bean.getId()) 这里面的值都会被设置到实例中
     */
    @Getter
    @Setter
    private String jobId;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobdetail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = mongoTemplate.findById(jobId, JobDataMap.class);
        if (jobDataMap == null) {
            return;
        }
        Object service = SpringUtil.getBean(jobDataMap.getClassName());
        try {
            Method method = service.getClass().getMethod(jobDataMap.getMethodName());
            method.invoke(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("job统一执行类");
    }

}
