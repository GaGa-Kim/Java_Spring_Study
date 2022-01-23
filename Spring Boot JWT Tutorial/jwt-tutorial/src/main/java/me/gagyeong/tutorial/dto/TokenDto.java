package me.gagyeong.tutorial.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto { // 토큰 정보를 Response할 때 사용할 클래스 생성

    private String token; // 토큰 필드를 가짐
}