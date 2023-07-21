package com.guoshunfa.springboot3quartzdetaileddemo.controller;

import com.guoshunfa.springboot3quartzdetaileddemo.entity.JobDataMap;
import com.guoshunfa.springboot3quartzdetaileddemo.service.JobDataMapService;
import com.guoshunfa.springboot3quartzdetaileddemo.service.QuartzService;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 定时任务服务调用
 **/
@RestController
public class JobController {

    @Resource
    private QuartzService quartzService;

    @Resource
    private JobDataMapService jobDataMapService;

    /**
     * 创建/修改定时任务信息
     *
     * @param jobData
     * @return
     */
    @PostMapping("/job/save")
    public JobDataMap saveJob(@RequestBody JobDataMap jobData) {
        JobDataMap data = jobDataMapService.save(jobData);
        quartzService.saveJob(data);
        return data;
    }

    /**
     * 删除定时任务
     *
     * @param jobId
     * @return
     */
    @DeleteMapping("/job/remove")
    public DeleteResult removeJob(@Nonnull String jobId) {
        DeleteResult deleteResult = jobDataMapService.remove(jobId);
        if (deleteResult.wasAcknowledged()) {
            JobDataMap data = jobDataMapService.findById(jobId);
            quartzService.removeJob(data);
        }
        return deleteResult;
    }

    /**
     * 暂停/运行定时任务
     *
     * @param jobId
     * @param paused
     * @return
     */
    @PutMapping("/job/paused")
    public UpdateResult pausedJob(@Nonnull String jobId, @Nonnull Boolean paused) {
        UpdateResult updateResult = jobDataMapService.updatePaused(jobId, paused);
        JobDataMap data = jobDataMapService.findById(jobId);
        quartzService.pausedJob(data);
        return updateResult;
    }
}

