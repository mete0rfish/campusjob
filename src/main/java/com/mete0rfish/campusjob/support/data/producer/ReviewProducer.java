package com.mete0rfish.campusjob.support.data.producer;

import com.mete0rfish.campusjob.domain.member.Member;
import com.mete0rfish.campusjob.domain.review.Review;
import com.navercorp.fixturemonkey.FixtureMonkey;
import lombok.extern.slf4j.Slf4j;
import net.jqwik.api.Arbitraries;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ReviewProducer {

    public List<Review> produce(Member member, final int count) {
        final List<Review> reviews = new ArrayList<>();
        FixtureMonkey fixtureMonkey = FixtureMonkey.create();
        for(int i=0;i<count;i++) {
            Review review = fixtureMonkey.giveMeBuilder(Review.class)
                    .setNull("id")
                    .setNull("version")
                    .setNull("member") // Set member to null initially
                    .set("company", "Company " + i)
                    .set("certificates", List.of("SQLD", "정보처리기사"))
                    .set("age", Arbitraries.integers().between(20, 30))
                    .set("seekPeriod", "4달")
                    .set("tip", "진짜 엄청 잘 열심히 노력")
                    .sample();
            review.setMember(member); // Set the member directly
            reviews.add(review);
        }

        return reviews;
    }
}
