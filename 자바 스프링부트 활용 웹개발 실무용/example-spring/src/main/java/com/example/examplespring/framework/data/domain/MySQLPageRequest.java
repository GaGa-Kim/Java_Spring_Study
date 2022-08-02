package com.example.examplespring.framework.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

/**
 * MySQL 페이지 요청 정보 및 계산된 값
 * @author gagyeong
 */
public class MySQLPageRequest {

    // page, size 정보로 자동으로 계산된 값이 limit과 offset에 들어가게 됨
    private int page;
    private int size;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private int limit;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private int offset;

    public MySQLPageRequest(int page, int size, int limit, int offset) {
        this.page = page;
        this.size = size;
        this.limit = limit;
        this.offset = offset;
    }
}
