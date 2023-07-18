package com.guoshunfa.springboot3quartzdetaileddemo.entity;

/**
 * job信息
 **/
public class JobDataMap extends BaseEntity {

    /**
     * job名称
     */
    private String jobName;

    /**
     * 类名称
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * cron规则
     */
    private String cron;

    /**
     * 任务状态，是否暂停。默认不暂停
     */
    private boolean paused = false;

    public void setClassName(String className) {
        this.className = className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
