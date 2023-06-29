package com.example.examplespring.mvc.controller;

import com.example.examplespring.mvc.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restapi/user")
public class UserRestController {

    // 비밀번호 예시 생성
    private static final String DATABASE_PASSWORD = "test2021";

    /**
     * 메소드에서 검증 로직을 구현할 때 Assert 클래스를 활욯하는 방법
     * Spring Assert.class + ControllerAdvice + MessageSource (다국어 프로퍼티 메시지) 조합
     * @param password
     * @return
     */
    @PostMapping("/confirm")
    public ResponseEntity<Member> confirm(@RequestParam String password) {
        // 실패일 경우
        // hasLength 메소드는 null 또는 공백일 경우 IllegalArgumentException 발생
        // password가 null, empty인 경우 2번째 인자에 원하는 에러 메시지를 입력
        Assert.hasLength(password, "member.password.hasLength");
        // isTrue 메소드는 패스워드가 일치하지 않으면 false 
        // password가 일치하지 않은 경우 2번째 인자에 원하는 에러 메시지를 입력
        Assert.isTrue(password.equals(DATABASE_PASSWORD), "member.password.isTrue");
        
        // 성공일 경우
        Member member = new Member();
        member.setName("개발자");
        member.setPhoneNumber("010-1234-3432");
        return new ResponseEntity<Member>(member, HttpStatus.OK);
    }
}
