package com.panda.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName BaseEntity
 * @Description 基础类
 * @Author guoshunfa
 * @Date 2021/11/23 上午11:43
 * @Version 1.0
 **/
@Data
@ApiModel(description = "基础类")
public class BaseEntity implements Serializable {

    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("逻辑删除，true：删除;false：未删除")
    private boolean xflag;

}
