package com.panda.base.result.test;

import com.panda.base.result.vo.ApiResult;
import com.panda.base.result.vo.NotUnifiedResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class ResultTestController {

    @GetMapping("/panda/test/result1")
    public String test1() {
        return "1";
    }

    @GetMapping("/panda/test/result2")
    public ApiResult<String> test2() {
        return ApiResult.ok("123");
    }

    @NotUnifiedResult
    @GetMapping("/panda/test/result3")
    public String test3() {
        return "test3";
    }

    @GetMapping("/panda/test/result4")
    public Date test4() {
        return new Date();
    }

}
