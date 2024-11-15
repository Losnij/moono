package com.example.moono.token;

import com.example.moono.token.JwtUtil;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization"); // 헤더에서 토큰 추출
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Bearer 이후 스트링 == 토큰
            if (jwtUtil.validateToken(token)) { // 유효성 검사
                String memberID = jwtUtil.extractMemberID(token); // 토큰 -> memberID 추출
                request.setAttribute("memberID", memberID);
            }
        }
        filterChain.doFilter(request, response);
    }
}
