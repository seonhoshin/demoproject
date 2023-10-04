package com.project.bbibbi.domain.showroom.feed.dto;

import com.project.bbibbi.domain.showroom.feedReply.dto.FeedReplyResponseDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponseDto {
    private Long feedId;

    private LocalDateTime createdDateTime;

    private LocalDateTime modifiedDateTime;

    private String title;

    private String content;

    private int views;

    private String coverPhoto;

    private String roomType;
    private String roomTypeName;
    private String roomSize;
    private String roomSizeName;
    private String roomCount;
    private String roomCountName;
    private String roomInfo;
    private String roomInfoName;
    private String location;
    private String locationName;

    private Long memberId;
    private String nickname;
    private String memberImage;
    private String myIntro;
    private int likeCount;
    private Boolean likeYn;
    private int repliesCount;
    private int bookMarkCount;
    private Boolean bookMarkYn;
    private Boolean followYn;

    // reply
    private List<FeedReplyResponseDto> replies;

}
