package com.project.bbibbi.domain.member.controller.dto;

import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.global.entity.SocialType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberProfile {

    private String username;
    private SocialType socialType; // 로그인한 서비스
    private String email;


    public Member toEntity() {
        return Member.builder()
                .nickname(this.username)
                .email(this.email)
                .socialType(this.socialType)
                .build();
    }
}
