package com.mete0rfish.campusjob.domain.member.dto;

import com.mete0rfish.campusjob.domain.member.Member;
import com.mete0rfish.campusjob.support.member.MemberRole;

public record LoginJoinRequest(
        String email,
        String name,
        String password
) {

    public Member toEntity(final String encodedPassword) {
        return new Member(email, name, encodedPassword, MemberRole.USER);
    }
}
