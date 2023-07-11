package com.panda.quartz.controller;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.panda.quartz.entity.JobDataMap;
import com.panda.quartz.service.QuartzService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName JobController
 * @Description job服务调用
 * @Author guoshunfa
 * @Date 2021/7/11 上午12:58
 * @Version 1.0
 **/
@RestController
public class JobController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private QuartzService quartzService;

    @PostMapping("/job/save")
    @ApiOperation(value = "创建/修改job信息")
    public JobDataMap saveJob(@RequestBody JobDataMap jobData) {
        JobDataMap data = mongoTemplate.save(jobData);
        quartzService.saveJob(data);
        return data;
    }

    @GetMapping("/job/remove")
    @ApiOperation(value = "删除job")
    public DeleteResult removeJob(@ApiParam(name = "jobId", required = true) String jobId) {
        DeleteResult deleteResult = mongoTemplate.remove(Query.query(Criteria.where("id").is(jobId)), JobDataMap.class);
        JobDataMap data = mongoTemplate.findById(jobId, JobDataMap.class);
        quartzService.removeJob(data);
        return deleteResult;
    }

    @GetMapping("/job/paused")
    @ApiOperation(value = "暂停/运行job")
    public UpdateResult pausedJob(
            @ApiParam(name = "jobId", required = true) String jobId
            , @ApiParam(name = "paused", required = true) Boolean paused) {
        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(jobId)),
                Update.update("paused", paused), JobDataMap.class);
        JobDataMap data = mongoTemplate.findById(jobId, JobDataMap.class);
        quartzService.pausedJob(data);
        return updateResult;
    }
}

