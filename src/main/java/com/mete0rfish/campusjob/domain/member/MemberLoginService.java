package com.mete0rfish.campusjob.domain.member;

import com.mete0rfish.campusjob.domain.member.dto.LoginJoinRequest;
import com.mete0rfish.campusjob.domain.member.dto.LoginRequest;
import com.mete0rfish.campusjob.domain.member.dto.MemberResponse;
import com.mete0rfish.campusjob.support.exception.CustomException;
import com.mete0rfish.campusjob.support.exception.ErrorCode;
import com.mete0rfish.campusjob.support.member.MemberRole;
import com.mete0rfish.campusjob.support.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        memberRepository.save(member);
        return MemberResponse.from(member);
    }

    public String login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if(!passwordEncoder.matches(request.password(), member.getPassword())) {
            // TODO 커스텀 예외처리
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return jwtUtil.createJwt(member.getEmail(), member.getRole().name());
    }
}
