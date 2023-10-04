package com.project.bbibbi.domain.member.service.dto.response;

import com.project.bbibbi.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponse {

    private Long memberId;
    private String email;
    private String nickname;
    private String profileImg;
    private String myIntro;

    public static MemberResponse MemberInfoResponse(Member member) {
        return MemberResponse.builder()
                .nickname(member.getNickname())
                .myIntro(member.getMyIntro())
                .profileImg(member.getProfileImg())
                .build();
    }
}
