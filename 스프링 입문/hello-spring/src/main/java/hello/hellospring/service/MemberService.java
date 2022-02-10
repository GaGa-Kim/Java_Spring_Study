package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// @Service
@Transactional
public class MemberService {

    // private final MemberRepository memberRepository = new MemoryMemberRepository();

    // MemberService의 MemoryMemberRepository와 MemberServiceTest의 MemoryMemberRepository는 인스턴스를 같게 하기 위해서
    private final MemberRepository memberRepository;

    // 직접 내가 new로 생성하는 것이 아니라 외부에서 넣어주도록 바꿔줌
    // @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 가입
    public Long join(Member member) {

        /* Ctrl + Alt + V 단축키
        Optional<Member> result = memberRepository.findByName(member.getName()); // 같은 이름이 있는 중복 회원이 불가능하도록
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }); */

        // Member member1 = result.get(); // 만약 같은 이름의 멤버가 있어서 바로 꺼낸다면 (또는 orElseGet()을 사용해 값이 있을 경우 꺼내고 아니면 else)

        /* 위를 간단하게 줄인 후 이를 Ctrl + Alt + M 단축키로 메소드로 뽑아내기
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }); */

        validateDuplicateMember(member); // 중복 회원 검증 메소드

        memberRepository.save(member);
        return member.getId();

        /* 메서드 호출 시간 측정
        long start = System.currentTimeMillis();

        try {
            validateDuplicateMember(member); // 중복 회원 검증 메소드

            memberRepository.save(member);
            return member.getId();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("join = " + timeMs + "ms");
        }
         */
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
    }

    // 전체 회원 조회
    public List<Member> findMembers() {
        return  memberRepository.findAll();

        /* 메서드 호출 시간 측정
        long start = System.currentTimeMillis();

        try {
            return  memberRepository.findAll();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("findMembers = " + timeMs + "ms");
        }
         */
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
