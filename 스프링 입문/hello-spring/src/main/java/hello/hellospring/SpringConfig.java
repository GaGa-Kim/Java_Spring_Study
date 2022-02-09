package hello.hellospring;

import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    /* Jdbc
    DataSource 는 데이터베이스 커넥션을 획득할 때 사용하는 객체
    스프링 부트는 데이터베이스 커넥션 정보를 바탕으로 DataSource를 생성하고 스프링 빈으로 만들어줌
    그래서 DI를 받아 제공해줄 수 있음 (스프링이 제공해주는 datasource 를 제공 받아 주입)
    private final DataSource dataSource;
    
    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }
     */

    /* JPA
    EntityManager 를 주입 받음 (DI)
    private EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
    }
     */

    /*
    MemberService와 MemberRepository를 스프링 빈에 등록한 후,
    스프링 빈에 등록되어 있는 MemberRepository를 MemberService 주입
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    // MemberRepository는 인터페이스, MemeoryMemberRepository는 구현체
    // 메모리에서 Jdbc로 변경
    // Jdbc에서 JdbcTemplate로 변경
    // JdbcTemplate에서 JPA로 변경
    public MemberRepository memberRepository() {
        // return new MemoryMemberRepository();
        // return new JdbcMemberRepository(dataSource);
        // return new JdbcTemplateMemberRepository(dataSource);
        // return new JpaMemberRepository(em);

    }
     */

    // 스프링 데이터 JPA
    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) { // 스프링 데이터 JPA가 만들어 놓은 구현체가 주입되어 등록
        this.memberRepository = memberRepository;
    }

    // JPA에서 SpringDataJPA로 변경
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository); // 주입 받은 것을 등록
    }

}
