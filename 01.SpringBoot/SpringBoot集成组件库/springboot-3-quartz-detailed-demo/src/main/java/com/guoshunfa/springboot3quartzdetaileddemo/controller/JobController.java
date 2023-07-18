package com.guoshunfa.springboot3quartzdetaileddemo.controller;

import com.guoshunfa.springboot3quartzdetaileddemo.entity.JobDataMap;
import com.guoshunfa.springboot3quartzdetaileddemo.service.QuartzService;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * job服务调用
 **/
@RestController
public class JobController {

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private QuartzService quartzService;

    /**
     * 创建/修改job信息
     *
     * @param jobData
     * @return
     */
    @PostMapping("/job/save")
    public JobDataMap saveJob(@RequestBody JobDataMap jobData) {
        JobDataMap data = mongoTemplate.save(jobData);
        quartzService.saveJob(data);
        return data;
    }

    /**
     * 删除job
     *
     * @param jobId
     * @return
     */
    @GetMapping("/job/remove")
    public DeleteResult removeJob(@Nonnull String jobId) {
        DeleteResult deleteResult = mongoTemplate.remove(Query.query(Criteria.where("id").is(jobId)), JobDataMap.class);
        JobDataMap data = mongoTemplate.findById(jobId, JobDataMap.class);
        quartzService.removeJob(data);
        return deleteResult;
    }

    /**
     * 暂停/运行job
     *
     * @param jobId
     * @param paused
     * @return
     */
    @GetMapping("/job/paused")
    public UpdateResult pausedJob(@Nonnull String jobId, @Nonnull Boolean paused) {
        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(jobId)),
                Update.update("paused", paused), JobDataMap.class);
        JobDataMap data = mongoTemplate.findById(jobId, JobDataMap.class);
        quartzService.pausedJob(data);
        return updateResult;
    }
}

