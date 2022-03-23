package jpabook.start;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="MEMBER", uniqueConstraints = {@UniqueConstraint(
        name = "NAME_AGE_UNIQUE",
        columnNames = {"NAME", "AGE"})})
public class Member {

    @Id
    @Column(name = "ID")
    private String id;

    /* 회원 이름 필수, 10자를 초과하면 안 되는 제약조건 추가
       nullable = false 속성을 통해 DDL에 not null 제약조건 추가
       length 속성을 통해 DDL에 문자의 크기를 지정
     */
    @Column(name = "NAME", nullable = false, length = 10)
    private String username;

    private Integer age;

    //=== 추가 ===
    /* 자바의 enum을 사용해서 회원 타입을 구분
       일반 회원은 USER, 관리자는 ADMIN
       enum을 사용하려면 @Enumerated 어노테이션으로 매핑 */
    @Enumerated(EnumType.STRING)
    private RoleType roleType; // 일반회원과 관리자로 구분

    /* 자바의 날짜 타입은 @Temporal을 사용해서 매핑 */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate; // 회원 가입일

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate; // 회원 수정일

    /* 길이 제한이 없도록 하기 위해서는 VARCHAR 대신 CLOB 타입으로 저장해야 함
        그러므로 @Lob를 사용해 CLOB, BLOB 타입을 매핑할 수 있도록 함 */
    @Lob
    private String description; // 길이 제한이 없는 회원 설명

    //Getter, Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
