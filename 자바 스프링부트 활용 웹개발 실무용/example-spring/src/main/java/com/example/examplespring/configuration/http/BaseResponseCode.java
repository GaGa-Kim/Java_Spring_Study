package com.example.examplespring.configuration.http;

public enum BaseResponseCode {

    SUCCESS, // 성공
    ERROR, // 실패
    LOGIN_REQUIRED, // 로그인
    DATA_IS_NULL, // NULL
    VALIDATE_REQUIRED, // 필수 체크
    UPLOAD_FILE_IS_NULL
    ;
}
