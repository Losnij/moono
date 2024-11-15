package com.example.moono.service;

import com.example.moono.domain.Member;
import com.example.moono.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 가입
    public String registerMember(String memberID, String password) {
        // 아이디 중복 여부 확인
        if (memberRepository.findByMemberID(memberID).isPresent()) {
            throw new IllegalArgumentException("중복된 아이디를 사용할 수 없습니다.");
        }

        Member member = new Member();
        member.setMemberID(memberID);
        member.setPassword(password);
        memberRepository.save(member);

        return "회원 가입 성공";
    }

    // 회원 탈퇴
    public void withdrawMember(String memberID) {
        Member member = memberRepository.findByMemberID(memberID)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        memberRepository.delete(member);
    }

    // 회원 조회
    public Member getMemberInfo(String memberID) {
        return memberRepository.findByMemberID(memberID)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    // 회원 정보 인증
    public boolean authenticate(String memberID, String password) {
        return memberRepository.findByMemberID(memberID)
                .map(member -> member.getPassword().equals(password)) // 비밀번호 일치 확인
                .orElse(false); // 회원이 없으면 false 반환
    }
}