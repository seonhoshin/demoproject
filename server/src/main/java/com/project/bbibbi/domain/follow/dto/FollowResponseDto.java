package com.project.bbibbi.domain.follow.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowResponseDto {
    private Long fromMemberId;
    private Long memberId;
    private Boolean followYn;

}
