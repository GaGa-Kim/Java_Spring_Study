package hello.hellospring.repository;

import hello.hellospring.domain.Member;
// import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    // 테스트 메서드 하나가 끝난 후 데이터를 clear
    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    // 회원 저장 테스트
    @Test
    public void save() {
        // given
        Member member = new Member();
        member.setName("spring");

        // when
        repository.save(member); // 저장

        // then
        Member result = repository.findById(member.getId()).get(); // 저장 시 셋팅된 아이디를 가져와
        // System.out.println("result = " + (result == member)); // result = true 글자로 출력
        // Assertions.assertEquals(result, member); // 같은지 비교하는 방법 1)
        assertThat(result).isEqualTo(member); // 같은지 비교하는 방법 2)
    }

    // name으로 회원 찾기 테스트
    @Test
    public void findByName() {
        // given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        // when
        Member result = repository.findByName("spring1").get();

        // then
        assertThat(result).isEqualTo(member1);
    }

    // 모든 회원 리스트 가져오기 테스트
    @Test
    public void findAll() {
        //given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        // when
        List<Member> result = repository.findAll();

        // then
        assertThat(result.size()).isEqualTo(2); // 갯수 비교를 통해 검증
    }
}
