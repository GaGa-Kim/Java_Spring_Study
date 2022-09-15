package com.example.examplespring.mvc.controller;

import com.example.examplespring.configuration.session.HttpSessionMember;
import com.example.examplespring.configuration.session.HttpSessionNiceAuth;
import com.example.examplespring.mvc.domain.Member;
import com.example.examplespring.mvc.domain.MemberType;
import com.example.examplespring.mvc.domain.SessionMember;
import com.example.examplespring.mvc.domain.SessionNiceAuth;
import com.example.examplespring.mvc.parameter.MemberSaveParameter;
import com.example.examplespring.mvc.repository.MemberMappingName;
import com.example.examplespring.mvc.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final HttpSessionMember httpSessionMember;
    private final HttpSessionNiceAuth httpSessionNiceAuth;

    @GetMapping("/save")
    public Member save() {
        Member member = new Member();
        member.setMemberId("user12345");
        member.setMemberType(MemberType.S);
        member.setEmail("test@naver.com");
        member.setZipcode("0121222222");
        member.setAddress("서울시");
        member.setJoinDate(Calendar.getInstance().getTime());
        member.setMemberState("N");
        member.setName("홍길동");
        member.setPassword("test");
        member.setPhoneNumber("0101234124");
        member.setUpdateDate(Calendar.getInstance().getTime());
        memberService.save(member);
        return member;
    }

    @GetMapping("/findById/{memberId}")
    public Member findById(@PathVariable String memberId) {
        return memberService.findById(memberId);
    }

    @GetMapping("/findByName/{name}")
    public List<Member> findByName(@PathVariable String name) {
        return memberService.findByName(name);
    }

    @GetMapping("/findNameMappingByName/{name}")
    public List<MemberMappingName> findNameMappingByName(@PathVariable String name) {
        return memberService.findNameMappingByName(name);
    }

    @GetMapping("/existsByName/{name}")
    public boolean existsByName(@PathVariable String name) {
        return memberService.existsByName(name);
    }

    @GetMapping("/countByName/{name}")
    public int countByName(@PathVariable String name) {
        return memberService.countByName(name);
    }

    @PostMapping("/niceauth/response")
    @ResponseBody
    public boolean niceAuthResponse(HttpServletRequest request) {
        // 실명인증 성공해서 응답이 온 경우 예시
        SessionNiceAuth niceAuth = new SessionNiceAuth();
        // 인증받은 식별코드
        niceAuth.setAuthId("3242213123213");
        niceAuth.setPhoneNumber("01012341234");
        niceAuth.setName("홍길동");
        // 로그인 시 세션에 정보 저장
        httpSessionNiceAuth.setAttribute(niceAuth);
        return true;
    }

    @PostMapping("/member/signup/save")
    @ResponseBody
    public boolean signupSave(@RequestParam String memberId) {
        // 회원가입 예시
        SessionNiceAuth niceAuth = httpSessionNiceAuth.getAttribute();
        // 보인인증이 안된 경우 예외처리
        if (niceAuth == null) {
            throw new RuntimeException("회원가입시 본인인증은 필수입니다.");
        }
        // DB에 저장될 정보 set (본인인증 정보까지)
        MemberSaveParameter member = new MemberSaveParameter();
        member.setAuthId(niceAuth.getAuthId());
        member.setName(niceAuth.getName());
        member.setPhoneNumber(niceAuth.getPhoneNumber());
        member.setMemberId(memberId);
        // 회원 DB에 저장 로직 처리 후
        // 보인인증 세션 초기화
        httpSessionNiceAuth.removeAttribute();
        return true;
    }

    @PostMapping("/login")
    @ResponseBody
    public boolean login(@RequestParam String memberId) {
        // DB 조회해서 실제 회원인 경우라고 가정하고 예시
        SessionMember member = new SessionMember();
        member.setMemberId(memberId);
        // 로그인 시 세션에 정보 저장
        httpSessionMember.setAttribute(member);
        return true;
    }
    
    @GetMapping("/mypage/info")
    public String info(Model model) {
        // 세션에 저장된 정보
        model.addAttribute("member", httpSessionMember.getAttribute());
        return "/mypage/info";
    }
    
    @GetMapping("/logout")
    public String logout() {
        // 로그아웃 시 모든 세션 정보 삭제
        httpSessionMember.invalidate();
        return "redirect:/main";
    }
}
