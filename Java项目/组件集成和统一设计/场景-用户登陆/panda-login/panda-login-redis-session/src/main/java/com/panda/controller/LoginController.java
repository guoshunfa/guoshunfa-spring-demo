package com.panda.controller;

import com.panda.base.IgnoreAuthorize;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@Validated
public class LoginController {

    @Valid
    @GetMapping("/panda/v1.0/login")
    @IgnoreAuthorize
    public String login(HttpServletRequest request, @NotBlank String username, @NotBlank String password) {
        if ("panda".equals(username) && "panda".equals(password)) {
            HttpSession session = request.getSession();
            return session.getId();
        }
        return "账号密码错误";
    }

    @GetMapping("/panda/v1.0/test")
    public String test(HttpSession session) {
        return session.getId();
    }

}
