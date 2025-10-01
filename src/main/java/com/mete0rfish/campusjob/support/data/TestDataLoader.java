package com.mete0rfish.campusjob.support.data;

import com.mete0rfish.campusjob.domain.member.Member;
import com.mete0rfish.campusjob.domain.member.MemberRepository;
import com.mete0rfish.campusjob.domain.review.ReviewRepository;
import com.mete0rfish.campusjob.support.data.producer.MemberProducer;
import com.mete0rfish.campusjob.support.data.producer.ReviewProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class TestDataLoader implements CommandLineRunner {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    private final ReviewProducer reviewProducer;
    private final MemberProducer memberProducer;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Member member = memberProducer.produceMe();
        Member userMember = memberProducer.produceUser("noway@gmail.com", "홍길동", "1234qwer");
        Member savedMember = memberRepository.save(userMember);
        memberRepository.save(member);
        reviewRepository.saveAll(reviewProducer.produce(savedMember, 10));
    }
}