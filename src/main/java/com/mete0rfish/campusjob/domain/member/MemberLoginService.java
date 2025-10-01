package com.mete0rfish.campusjob.domain.member;

import com.mete0rfish.campusjob.domain.member.dto.LoginJoinRequest;
import com.mete0rfish.campusjob.domain.member.dto.LoginRequest;
import com.mete0rfish.campusjob.domain.member.dto.MemberResponse;
import com.mete0rfish.campusjob.support.exception.CustomException;
import com.mete0rfish.campusjob.support.exception.ErrorCode;
import com.mete0rfish.campusjob.support.member.MemberRole;
import com.mete0rfish.campusjob.support.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberLoginService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponse createMember(LoginJoinRequest request) {
        Member member = new Member(
                request.email(),
                request.name(),
                passwordEncoder.encode(request.password()),
                MemberRole.USER
        );
        log.info("MemberLoginService: createMember: {}", member);
        memberRepository.save(member);
        return MemberResponse.from(member);
    }

    
}
