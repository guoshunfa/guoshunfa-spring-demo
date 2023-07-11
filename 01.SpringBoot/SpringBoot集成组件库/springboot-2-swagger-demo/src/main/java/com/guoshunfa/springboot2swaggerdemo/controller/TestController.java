package com.guoshunfa.springboot2swaggerdemo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("测试controller")
@RequestMapping("/guoshunfa")
public class TestController {

    @ApiOperation("测试接口")
    @GetMapping("/swagger/test/")
    public String test(@ApiParam("阿拉啦啦") String param1) {
        return "成功";
    }

}