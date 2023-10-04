package com.project.bbibbi.domain.goodTip.tipReply.dto;

import com.project.bbibbi.domain.goodTip.tipReply.entity.TipReply;
import lombok.*;

/**
 * DTO for {@link TipReply}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipReplyRequestDto {
    private Long tipReplyId;
    private String content;
    private Long tipId;
    private Long memberId;
    private String profileImg;
    private Boolean replyLikeYn;
}