package com.project.bbibbi.domain.showroom.feed.mapper;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.showroom.feed.dto.*;
import com.project.bbibbi.domain.showroom.feed.entity.Feed;
//import com.project.bbibbi.domain.feed.entity.FeedImage;
//import com.project.bbibbi.domain.feed.entity.FeedImageTag;
import com.project.bbibbi.domain.showroom.feedComment.dto.FeedCommentDto;
import com.project.bbibbi.domain.showroom.feedComment.entity.FeedComment;
import com.project.bbibbi.domain.showroom.feedReply.dto.FeedReplyResponseDto;
import com.project.bbibbi.domain.showroom.feedReply.entity.FeedReply;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.showroom.feed.dto.FeedPatchDto;
import com.project.bbibbi.domain.showroom.feed.dto.FeedPostDto;
import com.project.bbibbi.domain.showroom.feed.dto.FeedResponseDto;
import com.project.bbibbi.global.entity.*;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FeedMapper {
    default Feed feedPostDtoToFeed(FeedPostDto feedPostDto){
        if(feedPostDto == null){
            return null;
        }

        Feed feed = new Feed();

        feed.setTitle(feedPostDto.getTitle());
        feed.setContent(feedPostDto.getContent());
        feed.setCoverPhoto(feedPostDto.getCoverPhoto());
        feed.setRoomType(RoomType.valueOf(feedPostDto.getRoomType()));
        feed.setRoomSize(RoomSize.valueOf(feedPostDto.getRoomSize()));
        feed.setRoomCount(RoomCount.valueOf(feedPostDto.getRoomCount()));
        feed.setRoomInfo(RoomInfo.valueOf(feedPostDto.getRoomInfo()));
        feed.setLocation(Location.valueOf(feedPostDto.getLocation()));
//        feed.setMemberId(feedPostDto.getMemberId());

        Long memberId = loginUtils.getLoginId();
        Member member = Member.builder()
                .memberId(memberId)
                .build();
        feed.setMember(member);

        feed.setCreatedDateTime(LocalDateTime.now());

        return feed;

    }

    default Feed feedPatchDtoToFeed(FeedPatchDto feedPatchDto){
        if(feedPatchDto == null){
            return null;
        }

        Feed feed = new Feed();

        feed.setFeedId(feedPatchDto.getFeedId());
        feed.setTitle(feedPatchDto.getTitle());
        feed.setContent(feedPatchDto.getContent());
        feed.setCoverPhoto(feedPatchDto.getCoverPhoto());
        feed.setRoomType(RoomType.valueOf(feedPatchDto.getRoomType()));
        feed.setRoomSize(RoomSize.valueOf(feedPatchDto.getRoomSize()));
        feed.setRoomCount(RoomCount.valueOf(feedPatchDto.getRoomCount()));
        feed.setRoomInfo(RoomInfo.valueOf(feedPatchDto.getRoomInfo()));
        feed.setLocation(Location.valueOf(feedPatchDto.getLocation()));
//        feed.setMemberId(feedPatchDto.getMemberId());

        Long memberId = loginUtils.getLoginId();
        Member member = Member.builder()
                .memberId(memberId)
                .build();
        feed.setMember(member);

        feed.setModifiedDateTime(LocalDateTime.now());

        return feed;

    }

    default FeedResponseDto feedToFeedResponseDto(Feed feed){
        if(feed == null){
            return null;
        }

        FeedResponseDto feedResponseDto = new FeedResponseDto();
        feedResponseDto.setFeedId(feed.getFeedId());
        feedResponseDto.setCreatedDateTime(feed.getCreatedDateTime());
        feedResponseDto.setModifiedDateTime(feed.getModifiedDateTime());
        feedResponseDto.setTitle(feed.getTitle());
        feedResponseDto.setContent(feed.getContent());
        feedResponseDto.setViews(feed.getViews());
        feedResponseDto.setCoverPhoto(feed.getCoverPhoto());
        feedResponseDto.setRoomType(feed.getRoomType().toString());
        feedResponseDto.setRoomTypeName(feed.getRoomType().getDescription());
        feedResponseDto.setRoomSize(feed.getRoomSize().toString());
        feedResponseDto.setRoomSizeName(feed.getRoomSize().getDescription());
        feedResponseDto.setRoomCount(feed.getRoomCount().toString());
        feedResponseDto.setRoomCountName(feed.getRoomCount().getDescription());
        feedResponseDto.setRoomInfo(feed.getRoomInfo().toString());
        feedResponseDto.setRoomInfoName(feed.getRoomInfo().getDescription());
        feedResponseDto.setLocation(feed.getLocation().toString());
        feedResponseDto.setLocationName(feed.getLocation().getDescription());
        feedResponseDto.setMemberId(feed.getMember().getMemberId());
        feedResponseDto.setNickname(feed.getMember().getNickname());
        feedResponseDto.setMemberImage(feed.getMember().getProfileImg());
        feedResponseDto.setMyIntro(feed.getMember().getMyIntro());
        feedResponseDto.setLikeCount(feed.getLikeCount());
        feedResponseDto.setLikeYn(feed.getLikeYn());
        feedResponseDto.setBookMarkCount(feed.getBookMarkCount());
        feedResponseDto.setBookMarkYn(feed.getBookMarkYn());
        feedResponseDto.setRepliesCount( (feed.getReplies() == null) ? 0: feed.getReplies().size());
        feedResponseDto.setFollowYn(feed.getFollowYn());

        if(feed.getReplies() != null) {

            List<FeedReplyResponseDto> allReplies = new ArrayList<>();

            for (FeedReply feedReply : feed.getReplies()) {
                FeedReplyResponseDto feedReplyResponseDto = new FeedReplyResponseDto();
                feedReplyResponseDto.setFeedReplyId(feedReply.getFeedReplyId());
                feedReplyResponseDto.setFeedId(feedReply.getFeed().getFeedId());
                feedReplyResponseDto.setContent(feedReply.getContent());
                feedReplyResponseDto.setNickname(feedReply.getMember().getNickname());
                feedReplyResponseDto.setMemberId(feedReply.getMember().getMemberId());
                feedReplyResponseDto.setCreatedDateTime(feedReply.getCreatedDateTime());
                feedReplyResponseDto.setMemberImage(feedReply.getMember().getProfileImg());
                feedReplyResponseDto.setReplyLikeYn(feedReply.getReplyLikeYn());
                if (feedReply.getComments() != null) {
                    List<FeedCommentDto> allComments = new ArrayList<>();
                    for (FeedComment feedComment : feedReply.getComments()) {
                        FeedCommentDto commentDto = new FeedCommentDto();
                        commentDto.setFeedCommentId(feedComment.getFeedCommentId());
                        commentDto.setFeedReplyId(feedComment.getFeedReply().getFeedReplyId());
                        commentDto.setContent(feedComment.getContent());
                        commentDto.setMemberImage(feedComment.getMember().getProfileImg());
                        commentDto.setNickname(feedComment.getMember().getNickname());
                        commentDto.setMemberId(feedComment.getMember().getMemberId());
                        commentDto.setCreatedDateTime(feedComment.getCreatedDateTime());
                        allComments.add(commentDto);
                    }
                    feedReplyResponseDto.setComments(allComments);
                }
                allReplies.add(feedReplyResponseDto);
            }

            feedResponseDto.setReplies(allReplies);
        }
            return feedResponseDto;
    }

    default List<FeedResponseDto> feedsToFeedResponseDtos(List<Feed> feeds){

        List<FeedResponseDto> feedResponseDtos =
                feeds.stream()
                        .map(feed -> FeedResponseDto
                                .builder()
                                .feedId(feed.getFeedId())
                                .createdDateTime(feed.getCreatedDateTime())
                                .modifiedDateTime(feed.getModifiedDateTime())
                                .title(feed.getTitle())
                                .content(feed.getContent())
                                .views(feed.getViews())
                                .coverPhoto(feed.getCoverPhoto())
                                .roomType(feed.getRoomType().toString())
                                .roomTypeName(feed.getRoomType().getDescription())
                                .roomSize(feed.getRoomSize().toString())
                                .roomSizeName(feed.getRoomSize().getDescription())
                                .roomCount(feed.getRoomCount().toString())
                                .roomCountName(feed.getRoomCount().getDescription())
                                .roomInfo(feed.getRoomInfo().toString())
                                .roomInfoName(feed.getRoomInfo().getDescription())
                                .location(feed.getLocation().toString())
                                .locationName(feed.getLocation().getDescription())
                                .memberId(feed.getMember().getMemberId())
                                .nickname(feed.getMember().getNickname())
                                .memberImage(feed.getMember().getProfileImg())
                                .myIntro(feed.getMember().getMyIntro())
                                .likeCount(feed.getLikeCount())
                                .likeYn(feed.getLikeYn())
                                .bookMarkCount(feed.getBookMarkCount())
                                .bookMarkYn(feed.getBookMarkYn())
                                .repliesCount((feed.getReplies() == null) ? 0 : feed.getReplies().size())
                                .followYn(feed.getFollowYn())
                                .build())
                        .collect(Collectors.toList());

        return feedResponseDtos;
    }

}
