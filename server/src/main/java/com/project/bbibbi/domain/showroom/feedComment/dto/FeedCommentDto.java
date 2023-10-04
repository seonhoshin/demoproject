package com.project.bbibbi.domain.showroom.feedComment.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FeedCommentDto {

    private Long feedCommentId;
    private String content;
    private Long memberId;
    private Long feedReplyId;
    private Long parentComment;
    private Long commentOrder;
    private String nickname;
    private Long feedId;
    private LocalDateTime createdDateTime;
    private String memberImage;

}
