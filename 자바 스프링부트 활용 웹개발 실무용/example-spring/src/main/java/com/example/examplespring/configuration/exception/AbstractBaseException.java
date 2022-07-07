package com.example.examplespring.configuration.exception;

import com.example.examplespring.configuration.http.BaseResponseCode;

/**
 * 예외 처리 클래스를 커스텀으로 만들어서 사용하기 위한 에러 추상 클래스
 * 이를 상속받아서 구현하면 간단하게 예외 처리 클래스를 만들 수 있음
 */
public abstract class AbstractBaseException extends RuntimeException {

    private static final long serialVersionUID = 8342235231880246631L;

    protected BaseResponseCode responseCode;
    protected Object[] args;

    public AbstractBaseException() {
    }

    public AbstractBaseException(BaseResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public BaseResponseCode getResponseCode() {
        return responseCode;
    }

    public Object[] getArgs() {
        return args;
    }

}