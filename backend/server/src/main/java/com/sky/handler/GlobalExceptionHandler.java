package com.sky.handler;

import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for handling business exceptions in the project
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Catch business exceptions
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("Exception message: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * Catch runtime exceptions
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(RuntimeException ex){
        log.error("Runtime exception: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

}
