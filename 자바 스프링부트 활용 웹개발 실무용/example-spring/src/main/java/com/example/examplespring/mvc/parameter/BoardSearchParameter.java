package com.example.examplespring.mvc.parameter;

import com.example.examplespring.mvc.domain.BoardType;

import java.util.List;

/**
 * 게시물 검색 파라미터
 * @author gagyeong
 */
public class BoardSearchParameter {

    private String keyword;
    private List<BoardType> boardTypes;

    public BoardSearchParameter() {

    }
}
