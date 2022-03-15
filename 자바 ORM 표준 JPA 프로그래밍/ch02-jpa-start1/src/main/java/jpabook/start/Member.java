package jpabook.start;

import javax.persistence.*; // JPA 어노테이션의 패키지

/* @Entity
    클래스를 테이블과 매핑한다고 JPA에게 알려주며,
    @Entity가 사용된 클래스를 엔티티 클래스라고 함 */
@Entity
/* @Table
    엔티티 클래스에 매핑할 테이블 정보를 알려주며,
    여기서는 name 속성을 사용해 Member 엔티티를 MEMBER 테이블에 매핑
    생략시 클래스 이름을 테이블 이름으로 매핑 */
@Table(name="MEMBER")
public class Member {

    /* @Id
        엔티티 클래스의 필드를 테이블의 기본 키에 매핑하며,
        여기서는 엔티티의 id 필드를 테이블의 ID 기본 키 컬럼에 매핑
        @Id가 사용된 필드는 식별자 필드라고 함 */
    @Id
    @Column(name = "ID")
    private String id; // 아이디

    /* @Column
        필드를 컬럼에 매핑하며,
        여기서는 name 속성을 사용해서
        Member 엔티티의 username 필드를 MEMBER 테이블의 NAME 컬럼에 매핑 */
    @Column(name = "NAME")
    private String username; // 이름

    /* 매핑 정보가 없는 필드
        age 필드에는 매핑 어노테이션이 없으므로 (생략되어 있음)
        필드명을 사용해서 컬럼명으로 매핑하며,
        여기서는 필드명이 age이므로 age 컬럼으로 매핑 */
    private Integer age; // 나이

    // Getter, Setter
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
}
