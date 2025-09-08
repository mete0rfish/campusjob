package com.mete0rfish.campusjob.domain.review.dto;

import com.mete0rfish.campusjob.domain.review.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewResponse {

    private Long id;
    private List<String> certificates;
    private Integer age;
    private String seekPeriod;
    private String tip;

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .certificates(review.getCertificates())
                .age(review.getAge())
                .seekPeriod(review.getSeekPeriod())
                .tip(review.getTip())
                .build();
    }
}
