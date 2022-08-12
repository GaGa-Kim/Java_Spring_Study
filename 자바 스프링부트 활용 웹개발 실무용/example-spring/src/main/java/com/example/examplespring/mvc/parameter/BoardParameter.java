package com.example.examplespring.mvc.parameter;

import lombok.Data;

@Data
public class BoardParameter {

    private int boardSeq;
    private String title;
    private String contents;
    private boolean delYn;

    public BoardParameter() {

    }

    public BoardParameter(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
