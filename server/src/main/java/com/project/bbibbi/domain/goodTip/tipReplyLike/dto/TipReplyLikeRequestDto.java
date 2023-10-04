package com.project.bbibbi.domain.goodTip.tipReplyLike.dto;

import com.project.bbibbi.domain.goodTip.tipReplyLike.entity.TipReplyLike;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link TipReplyLike}
 */
@Getter
@Setter
@NoArgsConstructor
public class TipReplyLikeRequestDto {
    private Long tipReplyId;
    private Long memberId;
}
