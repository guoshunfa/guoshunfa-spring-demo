package com.guoshunfa.springboot3quartzdetaileddemo.service;

import com.guoshunfa.springboot3quartzdetaileddemo.entity.JobDataMap;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.stereotype.Service;

public interface JobDataMapService {

    /**
     * 增加定时任务
     *
     * @param jobData 定时任务属性
     * @return 定时任务属性，增加成功则存在id
     */
    JobDataMap save(JobDataMap jobData);

    /**
     * 根据id删除定时任务
     *
     * @param jobId 定时任务id
     * @return 删除结果
     */
    DeleteResult remove(String jobId);

    /**
     * 根据id查询定时任务信息
     *
     * @param jobId 定时任务id
     * @return 定时任务信息
     */
    JobDataMap findById(String jobId);

    /**
     * 暂停/运行定时任务
     * @param jobId 定时任务id
     * @param paused true:运行;false:暂停
     * @return
     */
    UpdateResult updatePaused(String jobId, Boolean paused);
}
