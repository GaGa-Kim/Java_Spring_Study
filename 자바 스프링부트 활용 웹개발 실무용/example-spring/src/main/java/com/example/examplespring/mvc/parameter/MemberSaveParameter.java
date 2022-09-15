package com.example.examplespring.mvc.parameter;

import lombok.Data;

@Data
public class MemberSaveParameter {

    private String authId;
    private String memberId;

    private String name;
    private String phoneNumber;
}
