package com.example.moono.controller;

import com.example.moono.service.MemberService;
import com.example.moono.token.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public AuthController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    // 멤버 로그인
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
