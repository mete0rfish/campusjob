package com.mete0rfish.campusjob.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMemberRequest {
    private String email;
    private String password;
    private String name;
}
