package com.mete0rfish.campusjob.domain.member;

import com.mete0rfish.campusjob.domain.member.dto.CreateMemberRequest;
import com.mete0rfish.campusjob.domain.member.dto.MemberResponse;
import com.mete0rfish.campusjob.domain.member.dto.UpdateMemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberApi {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        MemberResponse response = memberService.getMemberByEmail(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@RequestBody CreateMemberRequest request) {
        MemberResponse response = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/api/members/" + response.getId())).body(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long memberId) {
        MemberResponse response = memberService.getMember(memberId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long memberId, @RequestBody UpdateMemberRequest request) {
        MemberResponse response = memberService.updateMember(memberId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}