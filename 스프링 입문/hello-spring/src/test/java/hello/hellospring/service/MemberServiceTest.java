package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    /* MemberService memberService = new MemberService();
    MemoryMemberRepository memberRepository = new MemoryMemberRepository();
     */

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    // 테스트 실행 전에 각각 실행하여 테스트가 서로 영향이 없도록 항상 새로운 객체를 생성하고, 의존관계도 새로 맺어줌
    @BeforeEach
    public void beforeEach() {
        // MemoryMemverRespository를 MemberService의 MemberService에 넣어주어 같은 리포지토리를 사용하도록 함
        // 즉 MemberService 입장에서는 직접 new하는 것이 아니라, 외부에서 memberRepository를 넣어주므로 Dependency Injection (DI)
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    // 테스트 메서드 하나가 끝난 후 데이터(메모리)를 clear
    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        // given
        Member member = new Member();
        member.setName("hello");

        // when
        Long saveId = memberService.join(member);

        // then : 결과
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        // when
        memberService.join(member1);

        /*
        try {
            memberService.join(member2);
            // fail("예외가 발생해야 합니다.");
        } catch (IllegalStateException e) {
            // assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.123"); // 실패
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다."); // 성공
        }
         */

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}