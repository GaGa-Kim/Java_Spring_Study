package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {

    // 스프링부트가 엔티티매니저를 자동 생성
    // application.properties, DB 등의 정보를 통해 자동으로 생성
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) { // 만들어진 엔티티매니저를 주입
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member); // persist 를 통해 영속, 영구 저장 - JPA가 모든 것을 해 줌
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny(); // 하나만 반환
    }

    @Override
    public List<Member> findAll() {
        /* 인라인으로 변경 전
        List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();
        return result;
         */

        // 인라인으로 변경
        // JPQL 쿼리 언어 > 객체 (Member Entity) 자체를 대상으로 쿼리 (id나 name이 아닌 그 자체로) > SQL 로 번역이 됨
        // return em.createQuery("select m from Member as m", Member.class).getResultList();
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
}
