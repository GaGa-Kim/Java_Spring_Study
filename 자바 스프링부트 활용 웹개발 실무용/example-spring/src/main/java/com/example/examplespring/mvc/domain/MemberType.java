package com.example.examplespring.mvc.domain;

public enum MemberType implements BaseCodeLabelEnum {

    S("학생"),
    ;

    private String code;
    private String label;

    MemberType(String label) {
        this.code = name();
        this.label = label;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String label() {
        return label;
    }
}
