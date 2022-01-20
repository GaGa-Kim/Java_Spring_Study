package hello.hellospring;

import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    // MemberService와 MemberRepository를 스프링 빈에 등록한 후,
    // 스프링 빈에 등록되어 있는 MemberRepository를 MemberService 주입
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    // MemberRepository는 인터페이스, MemeoryMemberRepository는 구현체
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
