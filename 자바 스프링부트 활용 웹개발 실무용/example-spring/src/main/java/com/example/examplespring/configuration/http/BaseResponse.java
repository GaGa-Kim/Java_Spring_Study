package com.example.examplespring.configuration.http;

import lombok.Data;

/**
 * 성공일 경우 code, data 사용
 * 예외가 발생할 경우 code, message 사용
 * @param <T>
 */
@Data
public class BaseResponse<T> {

    private BaseResponseCode code;
    private String message;
    private T data;

    public BaseResponse(T data) { // 성공
        this.code = BaseResponseCode.SUCCESS;
        this.data = data;
    }

    public BaseResponse(BaseResponseCode code, String message) { // 예외처리
        this.code = code;
        this.message = message;
    }

}