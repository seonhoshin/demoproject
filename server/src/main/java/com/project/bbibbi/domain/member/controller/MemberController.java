package com.project.bbibbi.domain.member.controller;


import com.project.bbibbi.domain.member.controller.dto.MemberUpdateApiRequest;
import com.project.bbibbi.domain.member.controller.dto.MemberUpdatePasswordApiRequest;
import com.project.bbibbi.domain.member.service.MemberService;
import com.project.bbibbi.domain.member.service.dto.response.MemberResponse;
import com.project.bbibbi.global.response.ApiSingleResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;


@RestController
@RequestMapping("/members")
@Validated
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{member-id}")
    public ResponseEntity<ApiSingleResponse<MemberResponse>> getMember(@PathVariable("member-id") @Positive Long memberId) {

        MemberResponse memberResponse = memberService.getMember(memberId);

        return ResponseEntity.ok(ApiSingleResponse.ok(memberResponse));
    }


    @PatchMapping("/{member-id}")
    public ResponseEntity<Void> updateMember(@PathVariable("member-id") @Positive Long memberId,
                                             @RequestBody @Valid MemberUpdateApiRequest request) {

        memberService.updateMember(request.toUpdateServiceRequest(memberId));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{member-id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable("member-id") @Positive Long memberId,
                                               @RequestBody @Valid MemberUpdatePasswordApiRequest request) {

        memberService.updatePassword(request.toPasswordServiceRequest(memberId));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("member-id") @Positive Long memberId) {

         memberService.deleteMember(memberId);

        return ResponseEntity.noContent().build();
    }

}


