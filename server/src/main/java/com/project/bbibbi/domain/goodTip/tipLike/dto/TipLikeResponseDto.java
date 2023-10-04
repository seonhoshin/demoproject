package com.project.bbibbi.domain.goodTip.tipLike.dto;

import com.project.bbibbi.domain.goodTip.tipLike.entity.TipLike;
import lombok.*;

/**
 * DTO for {@link TipLike}
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipLikeResponseDto {
    private Long tipId;
    private Long memberId;
    private Boolean likeYn;
    private Integer likeCount;
}
