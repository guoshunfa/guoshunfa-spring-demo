package com.panda.quartz.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName JobData
 * @Description job信息
 * @Author guoshunfa
 * @Date 2021/7/10 下午10:33
 * @Version 1.0
 **/
@Data
@ApiModel(description = "job信息")
public class JobDataMap extends BaseEntity {

    @ApiModelProperty("job名称")
    private String jobName;

    @ApiModelProperty("类名称")
    private String className;

    @ApiModelProperty("方法名称")
    private String methodName;

    @ApiModelProperty("cron规则")
    private String cron;

    @ApiModelProperty("任务状态，是否暂停。默认不暂停")
    private boolean paused = false;

    public void setClassName(String className) {
        this.className = className.substring(0, 1).toLowerCase() + className.substring(1);
    }

}
