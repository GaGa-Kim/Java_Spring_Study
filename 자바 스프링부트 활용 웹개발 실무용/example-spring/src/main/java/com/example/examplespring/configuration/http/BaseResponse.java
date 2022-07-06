package com.example.examplespring.configuration.http;

import lombok.Data;

@Data
public class BaseResponse<T> {

    private BaseResponseCode code;
    private String message;
    private T data;

    public BaseResponse(T data) {
        this.code = BaseResponseCode.SUCCESS;
        this.data = data;
    }

}
