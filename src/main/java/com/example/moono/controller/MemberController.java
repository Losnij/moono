package com.example.moono.controller;

import com.example.moono.domain.Member;
import com.example.moono.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원 가입
    @PostMapping("/register")
    public ResponseEntity<String> registerMember(@RequestParam String memberID, @RequestParam String password) {
        String response = memberService.registerMember(memberID, password);
        return ResponseEntity.ok(response);
    }

    // 회원 탈퇴
    @DeleteMapping("/myPage")
    public ResponseEntity<String> withdrawMember(HttpServletRequest request) {
        String memberID = (String) request.getAttribute("memberID");
        memberService.withdrawMember(memberID);
        return ResponseEntity.ok("회원 탈퇴 성공");
    }

    // 회원 조회
    @GetMapping("/myPage")
    public ResponseEntity<Member> getMemberInfo(HttpServletRequest request) {
        String memberID = (String) request.getAttribute("memberID");
        return ResponseEntity.ok(memberService.getMemberInfo(memberID));
    }
}