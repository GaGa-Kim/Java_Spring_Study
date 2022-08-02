package com.example.examplespring.framework.data.domain;

import com.example.examplespring.framework.data.domain.MySQLPageRequest;
import lombok.Data;

/**
 * 페이지 요청 정보와 파라미터 정보
 * @author gagyeong
 * @param <T>
 */
@Data
public class PageRequestParameter<T> {

    private MySQLPageRequest pageRequest; // 페이징 정보
    private T parameter; // 검색조건 정보

    public PageRequestParameter(MySQLPageRequest pageRequest, T parameter) {
        this.pageRequest = pageRequest;
        this.parameter = parameter;
    }
}
