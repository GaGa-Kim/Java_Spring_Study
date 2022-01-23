package me.gagyeong.tutorial.config;

import me.gagyeong.tutorial.jwt.JwtAccessDeniedHandler;
import me.gagyeong.tutorial.jwt.JwtAuthenticationEntryPoint;
import me.gagyeong.tutorial.jwt.JwtSecurityConfig;
import me.gagyeong.tutorial.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity // 기본적인 web 보안 활성을 위한 어노테이션
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 어노테이션을 메소드 단위로 사용하기 위한 어노테이션
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig( // TokenProvider, JwtAuthenticationEntryPoint, JwtAccessDeniedHandler를 주입 받음
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers(
                        "/h2-console/**" // h2-console 하위 모든 요청들과
                        ,"/favicon.ico" // 파비콘 관련 요청은 Spring Security 로직을 수행하지 않도록 configure 메소드를 오버라이드
                        ,"/error"
                );
    }

    @Override // WebSecurityConfigurerAdapter의 configure 메소드를 오버라이드
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 토큰을 사용하는 방식이기 때문에 csrf를 disable
                .csrf().disable()

                // Exception을 핸들링할 때 아까 만든 401, 403 에러 관련 클래스 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console 설정 추가
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정
                .antMatchers("/api/hello").permitAll() // api/hello에 대한 요청은 인증없이 접근을 허용
                .antMatchers("/api/authenticate").permitAll() // 토큰이 없는 상태에서 요청이 들어오도록 하기 위해 로그인, 회원가입 API는 인증없이 접근을 허용
                .antMatchers("/api/signup").permitAll()
                .anyRequest().authenticated() // 나머지 요청들은 모두 인증되어야 함

                .and()
                .apply(new JwtSecurityConfig(tokenProvider)); // JwtFilter를 addFilterBefor로 등록했던 JwtSecurityConfig 클래스 적용
    }
}
