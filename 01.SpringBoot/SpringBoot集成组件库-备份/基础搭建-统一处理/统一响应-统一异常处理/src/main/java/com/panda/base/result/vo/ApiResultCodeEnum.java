package com.panda.base.result.vo;

/**
 * 响应码枚举
 */
public enum ApiResultCodeEnum {

    SUCCESS(200, "SUCCESS"),
    FAILURE(500, "FAILURE"),
    UN_LOGIN(401, "请求未授权"),
    NOT_FOUND(404, "接口不存在");

    private Integer code;

    private String msg;

    ApiResultCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
