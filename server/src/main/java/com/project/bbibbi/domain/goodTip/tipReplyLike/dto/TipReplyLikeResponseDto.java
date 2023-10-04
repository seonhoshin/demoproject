package com.project.bbibbi.domain.goodTip.tipReplyLike.dto;

import com.project.bbibbi.domain.goodTip.tipReplyLike.entity.TipReplyLike;
import lombok.*;

/**
 * DTO for {@link TipReplyLike}
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipReplyLikeResponseDto {
    private Long tipReplyId;
    private Long memberId;
    private Boolean replyLikeYn;
    private Integer likeCount;
}
