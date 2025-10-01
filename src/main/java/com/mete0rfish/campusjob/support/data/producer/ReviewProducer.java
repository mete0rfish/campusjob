package com.mete0rfish.campusjob.support.data.producer;

import com.mete0rfish.campusjob.domain.company.Company;
import com.mete0rfish.campusjob.domain.member.Member;
import com.mete0rfish.campusjob.domain.review.Review;
import com.mete0rfish.campusjob.support.company.CompanySize;
import com.mete0rfish.campusjob.support.member.MemberRole;
import com.navercorp.fixturemonkey.FixtureMonkey;
import lombok.extern.slf4j.Slf4j;
import net.jqwik.api.Arbitraries;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class ReviewProducer {

    public List<Review> produce(final int count) {
        final List<Review> reviews = new ArrayList<>();
        FixtureMonkey fixtureMonkey = FixtureMonkey.create();
        for(int i=0;i<count;i++) {
            Review review = fixtureMonkey.giveMeBuilder(Review.class)
                    .setNull("id")
                    .setNull("version")
                    .setNull("member")
                    .setNull("company")
                    .set("certificates", List.of("SQLD", "정보처리기사"))
                    .set("age", Arbitraries.integers().between(20, 30))
                    .set("seekPeriod", "4달")
                    .set("tip", "진짜 엄청 잘 열심히 노력")
                    .sample();
            reviews.add(review);
        }

        return reviews;
    }
}
