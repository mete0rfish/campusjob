package com.mete0rfish.campusjob.domain.member.dto;

import com.mete0rfish.campusjob.domain.member.Member;
import com.mete0rfish.campusjob.support.member.MemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String email;
    private String name;
    private MemberRole role;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole())
                .build();
    }
}