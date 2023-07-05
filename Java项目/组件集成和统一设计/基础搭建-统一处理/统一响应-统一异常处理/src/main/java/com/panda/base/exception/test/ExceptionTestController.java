package com.panda.base.exception.test;

import com.panda.base.exception.custom.PandaException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionTestController {

    @GetMapping("/panda/exception/test01")
    public void test01() {
        int a = 1 / 0;
    }

    @GetMapping("/panda/exception/test02")
    public void test02() {
        throw new RuntimeException("runtime");
    }

    @GetMapping("/panda/exception/test03")
    public void test03() {
        throw new PandaException("panda");
    }

}
