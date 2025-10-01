package com.mete0rfish.campusjob.support.filter;

import com.mete0rfish.campusjob.domain.member.Member;
import com.mete0rfish.campusjob.domain.member.MemberRepository;
import com.mete0rfish.campusjob.support.security.CustomUserDetails;
import com.mete0rfish.campusjob.support.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/api/login") || requestURI.equals("/api/join")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.toLowerCase().startsWith("bearer ")) {
            log.error("JwtFiler: token is null!");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);
        if (token.isEmpty()) {
            log.error("JwtFilter: token is empty!");
            filterChain.doFilter(request, response);
            return;
        }
        if(jwtUtil.isExpired(token)) {
            log.error("JwtFilter: token is expired!");
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtUtil.getEmail(token);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found in JWT"));

        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
