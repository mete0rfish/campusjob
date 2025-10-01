package com.mete0rfish.campusjob.domain.member;

import com.mete0rfish.campusjob.domain.member.dto.LoginJoinRequest;
import com.mete0rfish.campusjob.domain.member.dto.LoginRequest;
import com.mete0rfish.campusjob.domain.member.dto.MemberResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberLoginApi {

    private final MemberLoginService memberLoginService;

    @PostMapping("/api/join")
    public ResponseEntity<MemberResponse> createMember(@RequestBody LoginJoinRequest request) {
        MemberResponse response = memberLoginService.createMember(request);
        return ResponseEntity.ok(response);
    }

    
}
