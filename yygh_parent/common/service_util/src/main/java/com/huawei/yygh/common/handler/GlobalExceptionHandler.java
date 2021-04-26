package com.huawei.yygh.common.handler;

import com.huawei.yygh.common.exception.YyghException;
import com.huawei.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: Leslie
 * @create: 2021-04-26 18:46
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result error(Exception e ){
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(YyghException.class)
    public Result error(YyghException e ){
        e.printStackTrace();
        return Result.fail();
    }
}
