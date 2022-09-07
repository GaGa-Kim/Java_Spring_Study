package com.example.examplespring.mvc.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity(name = "T_MEMBER")
public class Member {

    @Id
    private String memberId;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;
    private String password;
    private String name;
    private String companyName;
    private String departName;
    private String email;
    private String telName;
    private String phoneNumber;
    private String zipcode;
    private String address;
    private String addressDetail;
    private boolean agreeTerms;
    private boolean agreeService;
    private boolean agreeEmail;
    private boolean agreeSns;
    private String memberState;
    private Date loginDate;
    private Date delDate;
    private Date joinDate;
    private Date updateDate;
}
