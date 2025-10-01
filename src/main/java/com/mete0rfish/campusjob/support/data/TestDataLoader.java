package com.mete0rfish.campusjob.support.data;

import com.mete0rfish.campusjob.domain.review.Review;
import com.mete0rfish.campusjob.domain.review.ReviewRepository;
import com.mete0rfish.campusjob.support.data.producer.ReviewProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TestDataLoader implements CommandLineRunner {

    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        reviewRepository.saveAll(ReviewProducer.produce(10));
    }
}
