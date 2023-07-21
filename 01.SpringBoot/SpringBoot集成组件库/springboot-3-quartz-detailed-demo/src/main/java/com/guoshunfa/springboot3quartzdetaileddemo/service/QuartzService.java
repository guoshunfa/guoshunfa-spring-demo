package com.guoshunfa.springboot3quartzdetaileddemo.service;

import com.guoshunfa.springboot3quartzdetaileddemo.entity.JobDataMap;

public interface QuartzService {
    public void saveJob(JobDataMap bean);
    public void removeJob(JobDataMap bean);
    public void pausedJob(JobDataMap bean);

}
