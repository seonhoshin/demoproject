package com.project.bbibbi.domain.showroom.feedReply.dto;


import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.global.entity.BaseEntity;
import io.jsonwebtoken.lang.Assert;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedReplyRequestDto extends BaseEntity {
    private Long feedReplyId;
    private String content;
    private Long memberId;
    private Long feedId;
    private String profileImg;
    private Boolean replyLikeYn;
    }



