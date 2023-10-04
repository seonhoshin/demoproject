package com.project.bbibbi.domain.showroom.feedReply.mapper;

import com.project.bbibbi.domain.showroom.feed.entity.Feed;
import com.project.bbibbi.domain.showroom.feedReply.dto.FeedReplyRequestDto;
import com.project.bbibbi.domain.showroom.feedReply.dto.FeedReplyResponseDto;
import com.project.bbibbi.domain.showroom.feedReply.entity.FeedReply;
import com.project.bbibbi.domain.member.entity.Member;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface FeedReplyMapper {

     default FeedReply feedReplyRequestDtoToFeedReply(FeedReplyRequestDto feedReplyRequestDto){

        if(feedReplyRequestDto == null){
            return null;
        }

        FeedReply feedReply = new FeedReply();

        feedReply.setMember(Member.builder().memberId(feedReplyRequestDto.getMemberId()).build());

        Feed feed = new Feed();
        feed.setFeedId(feedReplyRequestDto.getFeedId());
        feedReply.setFeed(feed);

        feedReply.setContent(feedReplyRequestDto.getContent());
        feedReply.setCreatedDateTime(LocalDateTime.now());

        return feedReply;

    }

    default FeedReplyResponseDto feedReplyToFeedReplyResponseDto(FeedReply feedReply){
        if(feedReply == null){
            return null;
        }

        FeedReplyResponseDto feedReplyResponseDto = new FeedReplyResponseDto();
        feedReplyResponseDto.setFeedReplyId(feedReply.getFeedReplyId());
        feedReplyResponseDto.setContent(feedReply.getContent());
        feedReplyResponseDto.setFeedId(feedReply.getFeed().getFeedId());
        feedReplyResponseDto.setMemberId(feedReply.getMember().getMemberId());
        feedReplyResponseDto.setNickname(feedReply.getMember().getNickname());
        feedReplyResponseDto.setMemberImage(feedReply.getMember().getProfileImg());
        feedReplyResponseDto.setCreatedDateTime(feedReply.getCreatedDateTime());

        return feedReplyResponseDto;

    }
}
