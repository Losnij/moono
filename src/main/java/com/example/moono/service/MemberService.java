package com.example.moono.service;

import com.example.moono.domain.Member;
import com.example.moono.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 아이디 중복 확인
    @Transactional
    public boolean existingMemberID(String memberID) {
        return memberRepository.findByMemberID(memberID).isPresent();
    }

    // 회원 가입
    @Transactional
    public void registerMember(String memberID, String password) {
        // 아이디 중복 여부 확인
        if (existingMemberID(memberID)) {
            throw new IllegalArgumentException("중복된 아이디를 사용할 수 없습니다.");
        }

        Member member = Member.builder()
                .memberID(memberID)
                .password(password)
                .build();
        memberRepository.save(member);
    }

    // 회원 탈퇴
    @Transactional
    public void withdrawMember(String memberID) {
        Member member = memberRepository.findByMemberID(memberID)
                .orElseThrow(() -> new IllegalArgumentException("권한이 없습니다."));
        memberRepository.delete(member);
    }

    // 회원 조회
    @Transactional
    public Member getMemberInfo(String memberID) {
        return memberRepository.findByMemberID(memberID)
                .orElseThrow(() -> new IllegalArgumentException("권한이 없습니다."));
    }

    // 회원 정보 인증
    @Transactional
    public boolean authenticate(String memberID, String password) {
        return memberRepository.findByMemberID(memberID)
                .map(member -> member.getPassword().equals(password)) // 비밀번호 일치 확인
                .orElse(false); // 회원이 없으면 false 반환
    }
}
