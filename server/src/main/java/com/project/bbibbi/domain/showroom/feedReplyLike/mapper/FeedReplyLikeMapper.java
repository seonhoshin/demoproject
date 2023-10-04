package com.project.bbibbi.domain.showroom.feedReplyLike.mapper;

import com.project.bbibbi.domain.showroom.feedReply.entity.FeedReply;
import com.project.bbibbi.domain.showroom.feedReplyLike.entity.FeedReplyLike;
import com.project.bbibbi.domain.showroom.feedReplyLike.dto.FeedReplyLikeRequestDto;
import com.project.bbibbi.domain.showroom.feedReplyLike.dto.FeedReplyLikeResponseDto;
import com.project.bbibbi.domain.member.entity.Member;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface FeedReplyLikeMapper {
    default FeedReplyLike feedReplyLikeRequestDtoToFeedReplyLike(FeedReplyLikeRequestDto feedReplyLikeRequestDto){
        if(feedReplyLikeRequestDto == null){
            return null;
        }

        FeedReplyLike feedReplyLike = new FeedReplyLike();

        FeedReply feedReply = new FeedReply();

        feedReply.setFeedReplyId(feedReplyLikeRequestDto.getFeedReplyId());
        feedReplyLike.setFeedReply(feedReply);
        feedReplyLike.setMember(Member.builder().memberId(feedReplyLikeRequestDto.getMemberId()).build());
        feedReplyLike.setCreatedDateTime(LocalDateTime.now());

        return feedReplyLike;

    }

    default FeedReplyLikeResponseDto feedReplyLikeToFeedReplyLikeResponseDto(FeedReplyLike feedReplyLike){
        if(feedReplyLike == null){
            return null;
        }

        FeedReplyLikeResponseDto feedReplyLikeResponseDto = new FeedReplyLikeResponseDto();

        feedReplyLikeResponseDto.setMemberId(feedReplyLike.getMember().getMemberId());
        feedReplyLikeResponseDto.setFeedReplyId(feedReplyLike.getFeedReply().getFeedReplyId());
        feedReplyLikeResponseDto.setReplyLikeYn(feedReplyLike.getReplyLikeYn());
        feedReplyLikeResponseDto.setLikeCount(feedReplyLike.getLikeCount());

        return feedReplyLikeResponseDto;
    }
}
