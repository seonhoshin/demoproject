package com.project.bbibbi.domain.showroom.feedReply.dto;

import com.project.bbibbi.domain.showroom.feedComment.dto.FeedCommentDto;
import com.project.bbibbi.global.entity.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FeedReplyResponseDto extends BaseEntity {

    private Long feedReplyId;
    private String content;
    private String nickname;
    private Long feedId;
    private Long memberId;
    private LocalDateTime createdDateTime;
    private List<FeedCommentDto> comments;
    private String memberImage;
    private Boolean replyLikeYn;
}
