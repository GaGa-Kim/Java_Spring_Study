package me.gagyeong.tutorial.controller;

import me.gagyeong.tutorial.dto.LoginDto;
import me.gagyeong.tutorial.dto.TokenDto;
import me.gagyeong.tutorial.jwt.JwtFilter;
import me.gagyeong.tutorial.jwt.TokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController { // 로그인 API를 추가하기 위한 클래스
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider; // TokenProvider와
        this.authenticationManagerBuilder = authenticationManagerBuilder; // AuthenticationManagerBuilder를 주입받음
    }

    @PostMapping("/authenticate") // 로그인 API 경로는 /api/authenticate 로 Post 요청을 받음
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        // LoginDto의 username, password를 파라미터로 받고
        // 이를 이용해 UsernamePasswordAuthenticationToken을 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        // authenticationToken을 이용해서 authenticate 메소드가 실행될 때
        // CustomUserDetailsService에서 loadUserMyUsername 메소드가 실행되어 Authentication 객체를 생성
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 생성된 Authentication 객체를 SecurityContext에 저장하고
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Authentication 객체의 인증정보를 기준으로 하여 createToken 메소드를 통해 JWT 토큰을 생성
        String jwt = tokenProvider.createToken(authentication);

        // JWT 토큰을 Response Header에도 넣어주고
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        // TokenDto를 이용해서 Response Body에도 넣어서 리턴
        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
