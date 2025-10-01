package com.mete0rfish.campusjob.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateReviewRequest {
    private String company;
    private List<String> certificates;
    private Integer age;
    private String seekPeriod;
    private String tip;
}
