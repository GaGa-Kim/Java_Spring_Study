package com.example.examplespring.mvc.domain;

/**
 * 기본 CodeLabelEnum
 * @author gagyeong
 */
public interface BaseCodeLabelEnum {

    /**
     * 코드를 리턴
     * @return
     */
    String code();

    /**
     * 라벨(코드명)을 리턴
     * @return
     */
    String label();

}
