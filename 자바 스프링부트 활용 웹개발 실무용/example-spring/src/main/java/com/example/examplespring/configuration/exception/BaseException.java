package com.example.examplespring.configuration.exception;

import com.example.examplespring.configuration.http.BaseResponseCode;

/**
 * 추상 클래스를 상속받아서 메세지 포맷을 동일하게 사용하도록 구현
 * 프로젝트에서 기본 예외 클래스를 사용하기 위한 클래스
 */
public class BaseException extends AbstractBaseException {

    private static final long serialVersionUID = 8342235231880246631L;

    public BaseException() {
    }

    public BaseException(BaseResponseCode responseCode) {
        this.responseCode = responseCode;
    }
    public BaseException(BaseResponseCode responseCode, String[] args) {
        this.responseCode = responseCode;
        this.args = args;
    }

}