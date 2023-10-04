package com.project.bbibbi.domain.goodTip.tipLike.dto;

import com.project.bbibbi.domain.goodTip.tipLike.entity.TipLike;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link TipLike}
 */
@Getter
@Setter
@NoArgsConstructor
public class TipLikeRequestDto {
    private Long tipId;
    private Long memberId;
}
