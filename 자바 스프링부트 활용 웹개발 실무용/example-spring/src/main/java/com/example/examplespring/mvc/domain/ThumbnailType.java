package com.example.examplespring.mvc.domain;

public enum ThumbnailType {

    WEB_MAIN(500, 300),
    WEB_SUB(150, 70),
    ;

    private int width;
    private int height;

    ThumbnailType(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }
}
