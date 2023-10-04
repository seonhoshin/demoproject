package com.project.bbibbi.domain.showroom.feedComment.mapper;

import com.project.bbibbi.domain.showroom.feed.entity.Feed;
import com.project.bbibbi.domain.showroom.feedComment.dto.FeedCommentDto;
import com.project.bbibbi.domain.showroom.feedComment.entity.FeedComment;
import com.project.bbibbi.domain.showroom.feedReply.entity.FeedReply;
import com.project.bbibbi.domain.member.entity.Member;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface FeedCommentMapper {

    default FeedComment feedCommentDtoToFeedComment(FeedCommentDto feedCommentDto){
        if(feedCommentDto == null){
            return null;
        }

        FeedComment feedComment = new FeedComment();

        Feed feed = new Feed();
        feed.setFeedId(feedCommentDto.getFeedId());
        feedComment.setFeed(feed);

        FeedReply feedReply = new FeedReply();
        feedReply.setFeedReplyId(feedCommentDto.getFeedReplyId());
        feedComment.setFeedReply(feedReply);

        feedComment.setMember(Member.builder().memberId(feedCommentDto.getMemberId()).build());

        feedComment.setContent(feedCommentDto.getContent());
        feedComment.setParentComment(feedCommentDto.getParentComment());
        feedComment.setCreatedDateTime(LocalDateTime.now());

        return feedComment;

    }

    default FeedCommentDto feedCommentToFeedCommentDto(FeedComment feedComment){
        if(feedComment == null){
            return null;
        }

        FeedCommentDto feedCommentDto = new FeedCommentDto();

        feedCommentDto.setFeedCommentId(feedComment.getFeedCommentId());
        feedCommentDto.setContent(feedComment.getContent());
        feedCommentDto.setFeedReplyId(feedComment.getFeedReply().getFeedReplyId());
        feedCommentDto.setFeedId(feedComment.getFeed().getFeedId());
        feedCommentDto.setMemberId(feedComment.getMember().getMemberId());
        feedCommentDto.setParentComment(feedComment.getParentComment());
        feedCommentDto.setNickname(feedComment.getMember().getNickname());
        feedCommentDto.setMemberImage(feedComment.getMember().getProfileImg());
        feedCommentDto.setCreatedDateTime(feedComment.getCreatedDateTime());

        return feedCommentDto;
    }
}
