package com.mete0rfish.campusjob.domain.member.dto;

import com.mete0rfish.campusjob.domain.member.Member;

public record MemberResponse(
        Long id,
        String email
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getEmail());
    }
}
