package com.project.bbibbi.domain.follow.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowListResponseDto {
    private Long fromMemberId;
    private String fromMemberNickname;
    private String fromMemberImage;
    private Long memberId;
    private String memberNickname;
    private String memberImage;
    private Boolean followYn;
    private LocalDateTime createdDateTime;
}
