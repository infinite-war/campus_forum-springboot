package com.example.common.handler;


import com.example.common.entity.Result;
import com.example.common.entity.StatusCode;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    //请求方式不支持
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        System.out.println(e.getMessage());
        return new Result(false, StatusCode.NOT_FOUND, "请求方式不支持");
    }


    //参数校验
    @ExceptionHandler({ConstraintViolationException.class})
    public Result handleConstraintViolationException(ConstraintViolationException ex) {
        System.out.println("handleConstraintViolationException");
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            PathImpl pathImpl = (PathImpl) constraintViolation.getPropertyPath();
            String paramName = pathImpl.getLeafNode().getName();
            String message = constraintViolation.getMessage();
            errors.put(paramName, message);
        }
        return new Result(false, StatusCode.PARAM_ERROR, "数据验证错误", errors);
    }

    //实体类DTO校验
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        System.out.println("handleMethodArgumentNotValidException");
        BindingResult bindingResult = ex.getBindingResult();
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : fieldErrors) {
                if (fieldError != null) {
                    String field = fieldError.getField();
                    String defaultMessage = fieldError.getDefaultMessage();
                    errors.put(field, defaultMessage);
                }
            }
            return new Result(false, StatusCode.PARAM_ERROR, "数据验证错误", errors);
        } else {
            return new Result(false, StatusCode.ERROR, "[" + ex.getCause() + "] " + ex.getMessage(), ex.getStackTrace());
        }
    }

    @ExceptionHandler({Exception.class})
    public Result handleException(Exception ex) {
        System.out.println("handleException");
        System.out.println(ex.getClass());
        return new Result(false, StatusCode.ERROR, "[" + ex.getCause() + "] " + ex.getMessage(), ex.getStackTrace());
    }
}