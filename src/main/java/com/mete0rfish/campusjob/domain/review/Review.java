package com.mete0rfish.campusjob.domain.review;

import com.mete0rfish.campusjob.domain.company.Company;
import com.mete0rfish.campusjob.domain.member.Member;
import com.mete0rfish.campusjob.support.converter.StringListConverter;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Convert(converter = StringListConverter.class)
    private List<String> certificates;
    private Integer age;
    private String seekPeriod;
    private String tip;
}
