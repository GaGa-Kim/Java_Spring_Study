package me.gagyeong.tutorial.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter // Lombok 어노테이션들
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto { // 로그인 시 사용할 클래스 생성

    @NotNull
    @Size(min = 3, max = 50) // @valid 관련 어노테이션 추가
    private String username;

    @NotNull
    @Size(min = 3, max = 100) // @valid 관련 어노테이션 추가
    private String password;
}