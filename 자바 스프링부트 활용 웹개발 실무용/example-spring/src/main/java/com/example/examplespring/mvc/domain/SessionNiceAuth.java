package com.example.examplespring.mvc.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "SessionNiceAuth")
public class SessionNiceAuth {

    @Id
    private String authId;

    private String name;
    private String phoneNumber;
}
