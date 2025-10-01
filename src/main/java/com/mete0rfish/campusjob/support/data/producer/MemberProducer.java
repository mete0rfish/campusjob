package com.mete0rfish.campusjob.support.data.producer;

import com.mete0rfish.campusjob.domain.member.Member;
import com.mete0rfish.campusjob.support.member.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberProducer {

    private final PasswordEncoder passwordEncoder;

    public Member produceMe() {
        return Member.builder()
                .email("sungwon326@naver.com")
                .name("윤성원")
                .role(MemberRole.USER)
                .password(passwordEncoder.encode("1254mlm"))
                .build();
    }

    public Member produceUser(final String email, final String name, final String password) {
        return Member.builder()
                .email(email)
                .name(name)
                .role(MemberRole.USER)
                .password(passwordEncoder.encode(password))
                .build();
    }
}
