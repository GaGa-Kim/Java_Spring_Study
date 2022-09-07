package com.example.examplespring.mvc.service;

import com.example.examplespring.mvc.domain.Member;
import com.example.examplespring.mvc.repository.MemberMappingName;
import com.example.examplespring.mvc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void save(Member member) {
        memberRepository.save(member);
    }

    public Member findById(String memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("회원이 없습니다."));
    }

    public List<Member> findByName(String name) {
        return memberRepository.findByName(name);
    }

    public List<MemberMappingName> findNameMappingByName(String name) {
        return memberRepository.findNameMappingByName(name);
    }

    public boolean existsByName(String name) {
        return memberRepository.existsByName(name);
    }

    public int countByName(String name) {
        return memberRepository.countByName(name);
    }
}
