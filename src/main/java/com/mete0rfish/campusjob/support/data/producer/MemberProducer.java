package com.mete0rfish.campusjob.support.data.producer;

import com.mete0rfish.campusjob.domain.member.Member;
import com.mete0rfish.campusjob.domain.review.Review;
import com.mete0rfish.campusjob.support.member.MemberRole;
import com.navercorp.fixturemonkey.FixtureMonkey;
import lombok.RequiredArgsConstructor;
import net.jqwik.api.Arbitraries;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MemberProducer {

    private final PasswordEncoder passwordEncoder;

    public Member produce() {
        return Member.builder()
                .email("sungwon326@naver.com")
                .name("윤성원")
                .role(MemberRole.USER)
                .password(passwordEncoder.encode("1254mlm"))
                .build();
    }
}
