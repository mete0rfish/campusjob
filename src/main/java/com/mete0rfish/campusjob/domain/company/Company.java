package com.mete0rfish.campusjob.domain.company;

import com.mete0rfish.campusjob.support.company.CompanySize;
import com.mete0rfish.campusjob.support.company.CompanySize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    @Enumerated(EnumType.STRING)
    private CompanySize companySize;

    private String salary;
    private Integer submitCount;
    private Integer passRate;
}
