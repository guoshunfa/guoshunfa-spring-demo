package com.panda.base.result.config;

import com.panda.base.result.vo.ApiResult;
import com.panda.base.result.vo.NotUnifiedResult;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 封装返回结果
 */
public class PandaHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler returnValueHandler;

    public PandaHandlerMethodReturnValueHandler(HandlerMethodReturnValueHandler returnValueHandler) {
        this.returnValueHandler = returnValueHandler;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnValueHandler.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        // 不需要统一封装的接口，直接返回。
        if (returnType.hasMethodAnnotation(NotUnifiedResult.class)) {
            this.returnValueHandler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            return;
        }
        // 人为的使用的统一封装，不会再次封装。
        if (returnValue instanceof ApiResult) {
            this.returnValueHandler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            return;
        }
        this.returnValueHandler.handleReturnValue(ApiResult.ok(returnValue), returnType, mavContainer, webRequest);
    }
}
