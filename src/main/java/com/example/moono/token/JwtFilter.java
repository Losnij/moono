package com.example.moono.token;

import com.example.moono.repository.MemberRepository;
import com.example.moono.service.MemberService;
import com.example.moono.token.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter { // 내부 API 요청시 재차 인증이 필요한 상황 방지

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.equals("/members/checkID")
                || path.equals("/members/register")
                || path.equals("/members/login")
                || path.startsWith("/h2-console")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization"); // 헤더에서 토큰 추출
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Bearer 이후 스트링 == 토큰
            if (jwtUtil.validateToken(token)) { // 유효성 검사
                String memberID = jwtUtil.extractMemberID(token); // 토큰 -> memberID 추출
                request.setAttribute("memberID", memberID);
            }
        }

        Object memberID = request.getAttribute("memberID");
        if (memberID == null || !memberService.existingMemberID((String) memberID)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write("권한이 없습니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
