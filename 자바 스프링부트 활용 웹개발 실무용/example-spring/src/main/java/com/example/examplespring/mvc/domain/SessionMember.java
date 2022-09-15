package com.example.examplespring.mvc.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "SessionMember")
public class SessionMember {

    @Id
    private String memberId;

    private String name;
    private String phoneNumber;
}
