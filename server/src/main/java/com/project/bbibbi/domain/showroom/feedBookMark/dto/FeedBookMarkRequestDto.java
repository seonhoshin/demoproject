package com.project.bbibbi.domain.showroom.feedBookMark.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedBookMarkRequestDto {
    private Long memberId;
    private Long feedId;
}
