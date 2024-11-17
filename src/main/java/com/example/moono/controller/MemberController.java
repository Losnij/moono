package com.example.moono.controller;

import com.example.moono.domain.Member;
import com.example.moono.dto.MemberResponseDto;
import com.example.moono.dto.MemberRequestDto;
import com.example.moono.service.MemberService;
import com.example.moono.token.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    // 중복 확인
    @GetMapping("/checkID")
    public ResponseEntity<String> checkExistingMember(@RequestParam String memberID) {
        if (memberService.existingMemberID(memberID)) {
            return ResponseEntity.ok("중복된 아이디입니다.");
        }
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }

    // 회원 가입
    @PostMapping("/register")
    public ResponseEntity<String> registerMember(@RequestBody MemberRequestDto memberRequestDto) {
        Member member = memberRequestDto.toEntity();
        memberService.registerMember(member.getMemberID(), member.getPassword());
        return ResponseEntity.ok("회원 가입 성공");
    }

    // 회원 탈퇴
    @DeleteMapping("/deregister")
    public ResponseEntity<String> withdrawMember(HttpServletRequest request) {
        String memberID = (String) request.getAttribute("memberID");
        memberService.withdrawMember(memberID);
        return ResponseEntity.ok("회원 탈퇴 성공");
    }

    // 회원 조회
    @GetMapping("/myPage")
    public ResponseEntity<MemberResponseDto> getMemberInfo(HttpServletRequest request) {
        String memberID = (String) request.getAttribute("memberID");
        MemberResponseDto memberResponseDto = MemberResponseDto.fromEntity(memberService.getMemberInfo(memberID));
        return ResponseEntity.ok(memberResponseDto);
    }

    // 회원 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberRequestDto memberRequestDto) {
        Member member = memberRequestDto.toEntity();
        if (memberService.authenticate(member.getMemberID(), member.getPassword())) { // 인증 성공
            String token = jwtUtil.generateToken(memberRequestDto.getMemberID());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("로그인 정보가 잘못되었습니다.");
        }
    }
}
