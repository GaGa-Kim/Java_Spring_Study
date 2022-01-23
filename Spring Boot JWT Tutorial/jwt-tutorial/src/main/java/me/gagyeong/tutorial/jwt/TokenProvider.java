package me.gagyeong.tutorial.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean { // 토큰의 생성과 토큰의 유효성을 담당하는 클래스

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long tokenValidityInMilliseconds;

    private Key key;

    public TokenProvider( // application.yml 으로부터 받아옴
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    @Override
    // InitializingBean을 implements해서 afterPropertiesSet을 Override 한 이유는
    // 빈이 생성되어 secret 값을 주입을 받은 후에 Base64 Decode해서 key 변수에 할당하기 위함 (application.yml에서 인코드해서 값을 받았으므로)
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret); // BASE64 디코드
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Spring Security에서 유저의 인증정보를 가지고 있는 Authentication 객체의 권한정보를 이용해서 토큰을 생성하는 메소드 (Authentication 객체 -> 토큰)
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds); // application.yml에서 설정했던 만료 시간을 설정하고

        return Jwts.builder() // JWT 토큰 생성
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    // 토큰에 담겨있는 정보를 이용해 Authentication 객체를 리턴하는 메소드 (토큰 -> Authenticaton 객체)
    public Authentication getAuthentication(String token) { // 토큰을 파라미터로 받아서
        Claims claims = Jwts // 토큰으로 클레임을 만들고
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities = // 클레임에서 권한 정보를 빼내어
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities); // 이용해 유저 객체를 만들어서

        return new UsernamePasswordAuthenticationToken(principal, token, authorities); // 유저 정보, 토큰, 권한 정보를 이용해 Authentication 객체 리턴
    }

    // 토큰을 파라미터로 받아서 유효성을 검사하는 메소드 (토큰 유효성 검사)
    public boolean validateToken(String token) { // 토큰을 파라미터로 받아서
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // 파싱해보고 발생하는 exception을 캐치
            return true; // 토큰이 정상이면 true
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false; // 토큰에 문제가 있으면 false
    }
}
