package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
// 스프링 컨테이너가 처음에 스프링을 뜰 때, @Controller 어노테이션으로 인해 MemberController 객체를 생성해서 스프링에 넣어두고 관리 됨
// 스프링 빈이 관리된다고 표현
public class MemberController {

    // private final MemberService memberService = new MemberService();
    // 위처럼이 객체를 new 해서 사용하지 말고, 하나만 생성해서 같이 쓰도록 하기 위해
    // 스프링 컨테이너로부터 MemberService를 가져다 쓰도록 코드 변경 (스프링 컨테이너에게 등록을 하고 사용)
    // Constructor 단축키 Alt + Insert
    private final MemberService memberService;

    @Autowired
    // @Autowired 어노테이션으로 인해 스프링이 컨테이너에 있는 MemberService를 가져다가 연결 시켜줌 (연관된 객체를 찾아 넣어줌)
    // 객체 의존관계를 외부에서 넣어주는 것을 DI (Dependency Injection), 의존성 주입
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
