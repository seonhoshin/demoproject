package com.project.bbibbi.domain.goodTip.tipComment.dto;

import com.project.bbibbi.domain.goodTip.tipComment.entity.TipComment;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for {@link TipComment}
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipCommentDto {

    private Long tipCommentId;

    private String content;

    private Long parentComment;

    private Long commentOrder;

    private String nickname;

    private Long tipId;

    private Long tipReplyId;

    private Long memberId;

    private String memberImage;

    private LocalDateTime createdDateTime;

    private LocalDateTime modifiedDateTime;

}