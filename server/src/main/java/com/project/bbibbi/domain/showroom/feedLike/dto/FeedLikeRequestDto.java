package com.project.bbibbi.domain.showroom.feedLike.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedLikeRequestDto {
    private Long memberId;
    private Long feedId;

}
