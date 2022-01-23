package me.gagyeong.tutorial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity // 데이터베이스 테이블과 1:1 매핑되는 객체를 위한 어노테이션
@Table(name = "user") // 테이블명을 user로 지정
@Getter // 롬복 어노테이션 - Get 관련 코드 자동 생성
@Setter // 롬복 어노테이션 - Set 관련 코드 자동 생성
@Builder // 롬복 어노테이션 - Builder 관련 코드 자동 생성
@AllArgsConstructor // 롬복 어노테이션 - Constructor 관련 코드 자동 생성
@NoArgsConstructor // 롬복 어노테이션 - Constructor 관련 코드 자동 생성
public class User {

    @JsonIgnore
    @Id
    @Column(name = "user_id") // 자동 증가되는 pk (primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", length = 50, unique = true) // username
    private String username;

    @JsonIgnore
    @Column(name = "password", length = 100) // password
    private String password;

    @Column(name = "nickname", length = 50) // nickname
    private String nickname;

    @JsonIgnore
    @Column(name = "activated") // 활성화 여부
    private boolean activated;

    // 권한들에 대한 관계
    @ManyToMany // User 객체와 권한 객체의 테이블의 다대다 관계를
    @JoinTable( // 일대다, 다대일 관계의 조인 테이블로 정의
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

}
