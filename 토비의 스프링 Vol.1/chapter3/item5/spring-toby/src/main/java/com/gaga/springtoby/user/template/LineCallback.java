package com.gaga.springtoby.user.template;

/**
 * 라인별 작업을 정의한 콜백 인터페이스
 */
public interface LineCallback<T> {
    // 타입 파라미터를 적용
    T doSomethingWithLine(String line, T value);
}
