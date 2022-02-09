package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// 스프링 데이터 JPA 가 자동으로 구현체를 만들어 주어 SpringDataJpaMemberRepository 를 스프링 빈에 자동으로 등록
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    // JPQL : select m from Member m where m.name = ?
    @Override
    Optional<Member> findByName(String name);
}
