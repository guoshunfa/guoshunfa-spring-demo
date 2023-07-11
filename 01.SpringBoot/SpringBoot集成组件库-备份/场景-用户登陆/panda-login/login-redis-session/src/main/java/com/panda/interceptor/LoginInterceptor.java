package com.panda.interceptor;

import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.panda.base.IgnoreAuthorize;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor, ApplicationContextAware {

    private static RedisOperationsSessionRepository sessionRepository;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        sessionRepository = SpringUtil.getBean(RedisOperationsSessionRepository.class);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 方法或者类上有注解，不需要登录。
        if (handlerMethod.getBeanType().isAnnotationPresent(IgnoreAuthorize.class) ||
                handlerMethod.getMethod().isAnnotationPresent(IgnoreAuthorize.class)) {
            return true;
        }
        String token = request.getHeader("token");
        String key = "spring:session:sessions:" + token;


        Map<Object, Object> mapSession = sessionRepository.getSessionRedisOperations().opsForHash().entries(key);

        if (MapUtil.isEmpty(mapSession)) {
            throw new RuntimeException("没有登陆，拒绝访问");
        }

        return true;
    }
}
