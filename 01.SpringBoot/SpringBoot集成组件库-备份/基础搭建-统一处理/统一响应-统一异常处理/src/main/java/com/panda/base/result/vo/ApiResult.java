package com.panda.base.result.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("接口响应内容")
public class ApiResult<T> {

    @ApiModelProperty("响应码")
    private Integer code;

    @ApiModelProperty("消息内容")
    private String msg;

    @ApiModelProperty("响应数据")
    private T data;

    public static ApiResult ok() {
        return ok(null);
    }

    public static <T> ApiResult<T> ok(T data) {
        return ok("请求成功", data);
    }

    public static <T> ApiResult<T> ok(String msg, T data) {
        return init(ApiResultCodeEnum.SUCCESS.getCode(), msg, data);
    }

    public static ApiResult fail() {
        return fail(null);
    }

    public static <T> ApiResult<T> fail(T data) {
        return fail("请求失败", data);
    }

    public static <T> ApiResult<T> fail(String msg, T data) {
        return init(ApiResultCodeEnum.FAILURE.getCode(), msg, data);
    }

    public static <T> ApiResult<T> init(Integer code, String msg, T data) {
        ApiResult<T> apiResult = new ApiResult<T>();
        apiResult.setCode(code);
        apiResult.setMsg(msg);
        apiResult.setData(data);
        return apiResult;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
