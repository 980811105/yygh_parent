package com.huawei.yygh.common.exception;

import com.huawei.yygh.common.result.Result;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: Leslie
 * @create: 2021-04-26 18:46
 **/
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public Result error(YyghException e) {
        e.printStackTrace();
        return Result.fail();
    }
}
