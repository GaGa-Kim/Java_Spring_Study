package me.gagyeong.tutorial.service;

import me.gagyeong.tutorial.entity.User;
import me.gagyeong.tutorial.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService { // Spring Security에서 중요한 부분 중 하나인 UserDetailService를 구현한 클래스

    // UserDetailsService를 implements 하고
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) { // UserRepository를 주입 받음
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    // loadUserByUsername 메소드를 Override해서
    public UserDetails loadUserByUsername(final String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username) // 로그인 시에 DB에서 유저정보와 권한정보를 가져오게 됨
                .map(user -> createUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createUser(String username, User user) {
        if (!user.isActivated()) { // 해당 정보를 기준으로 유저가 활성화 상태라면
            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), // 유저정보와 권한정보를 가지고 userdetails.User 객체를 생성해서 리턴
                user.getPassword(),
                grantedAuthorities);
    }
}
