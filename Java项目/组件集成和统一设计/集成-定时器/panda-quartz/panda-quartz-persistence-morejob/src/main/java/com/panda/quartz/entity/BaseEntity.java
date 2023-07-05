package com.panda.quartz.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName BaseEntity
 * @Description 请描述类的业务用途
 * @Author guoshunfa
 * @Date 2021/11/19 下午2:28
 * @Version 1.0
 **/
@Data
public class BaseEntity implements Serializable {

    @ApiModelProperty("主键id")
    private String id;

}
