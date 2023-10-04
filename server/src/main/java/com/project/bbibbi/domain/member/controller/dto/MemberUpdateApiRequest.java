package com.project.bbibbi.domain.member.controller.dto;

import com.project.bbibbi.domain.member.service.dto.request.MemberUpdateServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Size;
import java.util.List;
@AllArgsConstructor
@Getter
@Builder
public class MemberUpdateApiRequest {

    @Size(min = 2, max = 10, message = "2 ~ 10 글자 내로 입력해주세요.")
    private String nickname;
    private String myIntro;
    private String profileImg;

    public MemberUpdateServiceRequest toUpdateServiceRequest(Long memberId) {
        return MemberUpdateServiceRequest.builder()
                .memberId(memberId)
                .nickname(nickname)
                .myIntro(myIntro)
                .profileImg(profileImg)
                .build();
    }
}
