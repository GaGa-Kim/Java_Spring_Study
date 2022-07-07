package com.example.examplespring.configuration.web.bind.annotation;

import com.example.examplespring.configuration.exception.BaseException;
import com.example.examplespring.configuration.http.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

/**
 * 스프링에서 제공하는 컨트롤러어드바이스를 활용하여 예외가 발생하는 케이스에 대하여 정의
 * 컨트롤러, 서비스, 비즈니스 로직에서 BaseException 예외가 발생하면 handleBaseException 실행
 */
@ControllerAdvice
public class BaseControllerAdvice {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = { BaseException.class })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    private BaseResponse<?> handleBaseException(BaseException e, WebRequest request) {
        return new BaseResponse<String>(e.getResponseCode(), messageSource.getMessage(e.getResponseCode().name(), e.getArgs(), null));
    }

}