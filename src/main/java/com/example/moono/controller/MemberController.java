package com.example.moono.controller;

import com.example.moono.dto.MemberDto;
import com.example.moono.service.MemberService;
import com.example.moono.token.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
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
    public ResponseEntity<MemberDto> getMemberInfo(HttpServletRequest request) {
        String memberID = (String) request.getAttribute("memberID");
        MemberDto response = MemberDto.fromEntity(memberService.getMemberInfo(memberID));
        return ResponseEntity.ok(response);
    }

    // 회원 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String memberID, @RequestParam String password) {
        if (memberService.authenticate(memberID, password)) { // 인증 성공
            String token = jwtUtil.generateToken(memberID);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("로그인 정보가 잘못되었습니다.");
        }
    }
}