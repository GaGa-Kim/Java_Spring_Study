package me.gagyeong.tutorial.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity // 기본적인 web 보안 활성을 위한 어노테이션
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers(
                        "/h2-console/**" // h2-console 하위 모든 요청들과
                        ,"/favicon.ico" // 파비콘 관련 요청은 Spring Security 로직을 수행하지 않도록 configure 메소드를 오버라이드
                );
    }

    @Override // WebSecurityConfigurerAdapter의 configure 메소드를 오버라이드
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정
                .antMatchers("/api/hello").permitAll() // api/hello에 대한 요청은 인증없이 접근을 허용
                .anyRequest().authenticated(); // 나머지 요청들은 모두 인증되어야 함
    }
}
