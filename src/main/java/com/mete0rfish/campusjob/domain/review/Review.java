package com.mete0rfish.campusjob.domain.review;

import com.mete0rfish.campusjob.domain.company.Company;
import com.mete0rfish.campusjob.domain.member.Member;
import com.mete0rfish.campusjob.support.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
@ToString(exclude = {"member", "company"})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

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

    public void update(List<String> certificates, Integer age, String seekPeriod, String tip) {
        this.certificates = certificates;
        this.age = age;
        this.seekPeriod = seekPeriod;
        this.tip = tip;
    }
}
