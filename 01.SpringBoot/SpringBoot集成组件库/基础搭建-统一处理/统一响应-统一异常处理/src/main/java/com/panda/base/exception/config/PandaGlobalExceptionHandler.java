package com.panda.base.exception.config;

import com.panda.base.exception.custom.PandaException;
import com.panda.base.result.vo.ApiResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PandaGlobalExceptionHandler {

    /**
     * 这是我的自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler({PandaException.class})
    public ApiResult runtimeException(PandaException e) {
        return ApiResult.fail("PandaException:" + e);
    }

    @ExceptionHandler({Exception.class})
    public ApiResult Exception(Exception e) {
        return ApiResult.fail("Exception:" + e);
    }

}
