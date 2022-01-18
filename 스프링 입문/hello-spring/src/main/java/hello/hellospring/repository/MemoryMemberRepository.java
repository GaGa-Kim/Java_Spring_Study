package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); // 저장할 곳
    private static long sequence = 0L; // 키 값 생성

   @Override
    public Member save(Member member) {
        member.setId(++sequence); // store에 저장하기 전에 멤버의 id 값을 세팅하고
        store.put(member.getId(), member); // store에 저장 (Map에 저장)
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id)); // null도 반환될 수도 있도록 Optional 사용
    }

    @Override
    public Optional<Member> findByName(String name) {
        // 람다를 사용해 루프로 돌리면서 파라미터로 넘어온 name과 같은지 확인하여 필터링하여 반환
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
       // store에 있는 values인 Member들을 반환
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
       store.clear(); // store를 clear
    }
}
