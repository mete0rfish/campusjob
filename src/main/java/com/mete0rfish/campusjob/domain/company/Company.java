package com.mete0rfish.campusjob.domain.company;

import com.mete0rfish.campusjob.support.company.CompanySize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CompanySize companySize;

    private String salary;
    private Integer submitCount;
    private Integer passRate;
}
