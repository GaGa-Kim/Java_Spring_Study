package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    // Repository의 4가지 기능
    Member save(Member member); // 회원 저장
    Optional<Member> findById(Long id); // id로 회원 찾기
    Optional<Member> findByName(String name); // name으로 회원 찾기
    List<Member> findAll(); // 모든 회원 리스트
}
