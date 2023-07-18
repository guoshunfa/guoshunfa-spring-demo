package com.guoshunfa.springboot3quartzdetaileddemo.entity;

import java.io.Serializable;

/**
 * 基础类
 **/
public class BaseEntity implements Serializable {

    /**
     * 主键id
     */
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
