package me.gagyeong.tutorial.jwt;


import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
// AuthenticationEntryPoint를 implements 한 후
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint { // 유효한 자격증명을 제공하지 않고 접근할 때 401 Unauthorized 에러를 리턴할 JwtAuthenticatonEntryPoint 클래스

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401 에러를 전송
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
