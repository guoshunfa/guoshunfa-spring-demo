package com.guoshunfa.springboot3swaggerdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "测试controller", description = "测试")
@RequestMapping("/guoshunfa")
public class TestController {

    @Operation(summary = "测试接口", description = "测试接口")
    @GetMapping("/swagger/test/")
    public String test(@Parameter(name = "param1", description = "阿拉啦啦") String param1) {
        return "成功";
    }

}