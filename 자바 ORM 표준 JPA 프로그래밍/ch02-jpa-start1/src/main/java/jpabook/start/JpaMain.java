package jpabook.start;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        /* 엔티티 매니저 팩토리 생성
            JPA를 시작하려면 persistence.xml의 설정 정보를 사용해서 엔티티 매니저 팩토리를 생성
            Persistence 클래스를 사용해 엔티티 매니저 팩토리를 생성해서 JPA를 사용할 수 있게 준비
            persistence.xml의 설정 정보를 읽어서 JPA를 동작시키기 위한 기반 객체를 만들고
            JPA 구현체에 따라 데이터베이스 커넥션 풀도 생성하므로 비용이 아주 커 전체에 한 번만 생성하고 공유해서 사용
            META-INT/persistence.xml에서 이름이 jpabook인 영속성 유닛을 찾아서 엔티티 매니저 팩토리 생성 */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        /* 엔티티 매니저 생성
            엔티티 매니저 팩토리에서 엔티티 매니저를 생성하며 JPA의 기능 대부분은 엔티티 매니저가 제공
            대표적으로 엔티티 매니저를 사용해서 엔티티를 데이터베이스에 등록/수정/삭제/조회 가능
            엔티티 매니저는 내부에서 데이터소스(커넥션)를 유지하면서 데이터베이스와 통신
            엔티티 매니저는 데이터베이스 커넥션과 밀접한 관계이므로 스레드 간에 공유하거나 재사용을 하면 안 됨 */
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction(); // 트랜잭션 기능 획득 (트랜잭션 API)

        try {


            tx.begin(); // 트랜잭션 시작
            logic(em);  // 비즈니스 로직 실행
            tx.commit(); // 트랜잭션 커밋

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback(); // 예외 발생 시 트랜잭션 롤백
        } finally {
            /* 엔티티 매니저 종료
                마지막으로 사용이 끝난 엔티티 매니저는 반드시 종료해야 함 */
            em.close();
        }

        /* 엔티티 매니저 팩토리 종료
            애플리케이션을 종료할 때 엔티티 매니저 팩토리도 종료해야 함 */
        emf.close();
    }

    // 비즈니스 로직
    public static void logic(EntityManager em) {

        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("지한");
        member.setAge(2);

        /* 등록
            엔티티 매니저와 persist() 메소드에 저장할 엔티티를 넘겨줌
            JPA는 회원 엔티티의 매핑 정보를 분석해 SQL을 만들어 데이터베이스에 전달
            INSERT INTO MEMBER (ID, NAME, AGE) VALUES ('id1', '지한', 2) */
        em.persist(member);

        /* 수정
            JPA는 어떤 엔티티가 변경되었는지 추적 기능을 갖추고 있으므로
            엔티티의 값만 변경하면 UPDATE SQL을 생성해서 데이터베이스에 값을 변경
            UPDATE MEMBER
                SET AGE=20, NAME='지한'
            WHERE ID='id1' */
        member.setAge(20);

        /* 한 건 조회
            find() 메소드는 조회할 엔티티 타입과 @Id로
            데이터베이스 테이블의 기본 키와 매핑한 식별자 값으로 엔티티 하나를 조회
            이 메소드를 호출하면 SELECT SQL을 생성해서 데이터베이스에 결과를 조회하고
            결과 값으로 엔티티를 생성해서 반환
            SELECT * FROM MEMBER WHERE ID='id1' */
        Member findMember = em.find(Member.class, id);
        System.out.println("findMember=" + findMember.getUsername() + ", age=" + findMember.getAge());

        /* 목록 조회
            데이터베이스 테이블을 대상으로 쿼리하는 SQL과 달리, 엔티티 객체를 대상으로 검색하는 JPA에서는
            엔티티 객체(클래스와 필드)를 대상으로 검색하기 위해 검색 조건이 포함된 SQL인 JPQL 쿼리 언어 사용
            JPA는 SQL을 추상화하는 JPQL이라는 객체지향 쿼리 언어 제공
            MEMBER 테이블이 아닌, 회원 엔티티 객체로부터 검색
            JPQL을 사용하려면 먼저 em.createQuery(JPQL, 반환 타입) 메소드를 실행해서 쿼리 객체를 생성한 후
            쿼리 객체의 getResultList() 메소드를 호출
            JPA는 JPQL을 분석해서 적절한 SQL을 만들어 데이터베이스에서 데이터를 조회
            SELECT M.ID, M.NAME, M.AGE FROM MEMBER M
         */
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        System.out.println("members.size=" + members.size());

        /* 삭제
            엔티티를 삭제하려면 엔티티 매니저의 remove() 메소드에 삭제하려는 엔티티를 넘겨줌
            JPA는 DELETE SQL을 생성해서 실행
            DELETE FROM MEMBER WHERE ID = 'id1' */
        em.remove(member);

    }
}
