package jpabook.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity // 회원 엔티티
public class Member {

    /* 식별자 자동 생성, 기본 생성 전략인 AUTO이므로
       H2 데이터베이스에서는 SEQUENCE 사용 */
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name; // 이름

    private String city; // 주소 CITY, STREET, ZIPCODE
    private String street;
    private String zipcode;

    //Getter, Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
