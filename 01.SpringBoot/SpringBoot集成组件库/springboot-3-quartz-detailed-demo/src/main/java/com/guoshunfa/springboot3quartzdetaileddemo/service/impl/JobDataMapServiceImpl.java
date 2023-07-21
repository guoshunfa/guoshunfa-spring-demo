package com.guoshunfa.springboot3quartzdetaileddemo.service.impl;

import com.guoshunfa.springboot3quartzdetaileddemo.entity.JobDataMap;
import com.guoshunfa.springboot3quartzdetaileddemo.service.JobDataMapService;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import jakarta.annotation.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class JobDataMapServiceImpl implements JobDataMapService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public JobDataMap save(JobDataMap jobData) {
        return mongoTemplate.save(jobData);
    }

    @Override
    public DeleteResult remove(String jobId) {
        return mongoTemplate.remove(Query.query(Criteria.where("id").is(jobId)), JobDataMap.class);
    }

    @Override
    public JobDataMap findById(String jobId) {
        return mongoTemplate.findById(jobId, JobDataMap.class);
    }

    @Override
    public UpdateResult updatePaused(String jobId, Boolean paused) {
        return mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(jobId)),
                Update.update("paused", paused), JobDataMap.class);
    }
}
