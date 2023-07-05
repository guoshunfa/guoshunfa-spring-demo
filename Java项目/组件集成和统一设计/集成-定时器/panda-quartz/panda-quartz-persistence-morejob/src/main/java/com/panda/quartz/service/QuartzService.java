package com.panda.quartz.service;

import com.panda.quartz.config.JobAutoExe;
import com.panda.quartz.entity.JobDataMap;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;

/**
 * @ClassName JobController
 * @Description job服务调用
 * @Author guoshunfa
 * @Date 2021/7/10 下午10:49
 * @Version 1.0
 **/
@Service
public class QuartzService {

    @Autowired
    private Scheduler scheduler;

    /**
     * 保存/修改job
     * @param bean
     */
    public void saveJob(JobDataMap bean) {
        try {
            JobKey jobKey = new JobKey(bean.getJobName());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                jobDetail = newJobDetail(bean);
            }
            Trigger trigger = newCronTrigger(bean);
            if (trigger != null) {
                scheduler.scheduleJob(jobDetail, trigger);
            }
            cronUpdate(bean);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 移除job
     * @param bean
     */
    public void removeJob(JobDataMap bean) {
        try {
            TriggerKey key = new TriggerKey(bean.getJobName());
            scheduler.pauseTrigger(key);
            scheduler.unscheduleJob(key);
            JobKey jobKey = new JobKey(bean.getJobName());
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void pausedJob(JobDataMap bean) {
        try {
            if (bean.isPaused()) {
                scheduler.pauseTrigger(new TriggerKey(bean.getJobName()));
            } else {
                scheduler.resumeTrigger(new TriggerKey(bean.getJobName()));
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * cron 调整
     * @param bean
     */
    private void cronUpdate(JobDataMap bean) {
        try {
            JobKey jobKey = new JobKey(bean.getJobName());
            JobDetail quartzJob = scheduler.getJobDetail(jobKey);
            if (quartzJob == null) {//没有任务时创建任务并调度
                quartzJob = newJobDetail(bean);
                Trigger trigger = newCronTrigger(bean);
                if (trigger != null) {
                    scheduler.scheduleJob(quartzJob, trigger);
                }
            } else {//重新调度
                TriggerKey triggerKey = new TriggerKey(bean.getJobName());
                Trigger trigger = newCronTrigger(bean);
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JobDetail newJobDetail(JobDataMap bean) throws InstantiationException, IllegalAccessException {
        JobDetail jobDetail = JobBuilder.newJob(JobAutoExe.class)
                .withIdentity(bean.getJobName()).build();
        jobDetail.getJobDataMap().put("jobId", bean.getId());//job实例执行前会将这些属性值设置进实例中，运行前根据id查询详情
        return jobDetail;
    }

    /**
     * 根据调度设置决定使用cron还是毫秒数
     *
     * @param bean
     * @return
     * @throws ParseException
     */
    private Trigger newCronTrigger(JobDataMap bean) throws ParseException {
        if (StringUtils.isEmpty(bean.getCron())) {
            return null;
        }
        return TriggerBuilder.newTrigger().forJob(bean.getJobName())
                .withIdentity(bean.getJobName())
                .withSchedule(CronScheduleBuilder.cronSchedule(bean.getCron()))
                .build();
    }

}

