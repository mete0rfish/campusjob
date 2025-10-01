package com.mete0rfish.campusjob.domain.member;

import com.mete0rfish.campusjob.support.member.MemberRole;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.Builder;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String password;
    private MemberRole role;

    public Member(String email, String name, String password, MemberRole role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public void updateMember(String email, MemberRole role) {
        this.email = email;
        this.role = role;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
