package me.gagyeong.tutorial.service;

import me.gagyeong.tutorial.dto.UserDto;
import me.gagyeong.tutorial.entity.Authority;
import me.gagyeong.tutorial.entity.User;
import me.gagyeong.tutorial.repository.UserRepository;
import me.gagyeong.tutorial.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService { // 회원 가입, 유저정보조회 등의 메소드를 만들기 위한 클래스
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository; // UserRepository와
        this.passwordEncoder = passwordEncoder; // PasswordEncoder를 주입 받음
    }

    @Transactional
    // 회원가입 로직을 수행하는 메소드
    public User signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        // 파라미터로 받은 username이 DB에 존재하지 않으면 Authority와 USer 정보를 생성해서
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER") // signup 메소드를 통해 가입한 회원은 ROLE_USER
                .build(); // ↔ data.sql에서 자동생성되는 admin 계쩡은 ROLE_USER, ROLE_ADMIN 권한을 모두 가짐

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        // UserRepository의 save 메소드를 통해 DB에 정보를 저장
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    // 파라미터로 받아온 username을 기준으로 유저와 권한 정보를 가져옴
    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    // 현재 SecurityContext에 저장된 username만을 기준으로 유저와 권한정보를 가져옴
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }
}
