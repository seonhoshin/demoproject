package com.project.bbibbi.domain.showroom.feedReplyLike.controller;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.showroom.feedReplyLike.dto.FeedReplyLikeRequestDto;
import com.project.bbibbi.domain.showroom.feedReplyLike.dto.FeedReplyLikeResponseDto;
import com.project.bbibbi.domain.showroom.feedReplyLike.entity.FeedReplyLike;
import com.project.bbibbi.domain.showroom.feedReplyLike.mapper.FeedReplyLikeMapper;
import com.project.bbibbi.domain.showroom.feedReplyLike.service.FeedReplyLikeService;
import com.project.bbibbi.global.response.SingleResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feed/feedReply/{feed-reply-id}/feedReplyLike")
@Validated
public class FeedReplyLikeController {

    private final static String FEED_REPLY_LIKE_DEFAULT_URL = "/feed/feedReply/{feed-reply-id}/feedReplyLike";

    private final FeedReplyLikeService feedReplyLikeService;

    private final FeedReplyLikeMapper mapper;

    public FeedReplyLikeController(FeedReplyLikeService feedReplyLikeService, FeedReplyLikeMapper mapper) {
        this.feedReplyLikeService = feedReplyLikeService;
        this.mapper = mapper;
    }

    @PatchMapping
    public ResponseEntity patchFeedReplyLike(@PathVariable("feed-reply-id") Long feedReplyId){

        Long memberId = loginUtils.getLoginId();

        FeedReplyLikeRequestDto requestBody = new FeedReplyLikeRequestDto();

        requestBody.setMemberId(memberId);
        requestBody.setFeedReplyId(feedReplyId);

        FeedReplyLike feedReplyLike = mapper.feedReplyLikeRequestDtoToFeedReplyLike(requestBody);

        FeedReplyLike updatedFeedReplyLike = feedReplyLikeService.settingFeedReplyLike(feedReplyLike);

        FeedReplyLikeResponseDto feedReplyLikeResponseDto = mapper.feedReplyLikeToFeedReplyLikeResponseDto(updatedFeedReplyLike);

        return new ResponseEntity<>(new SingleResponseDto<>(feedReplyLikeResponseDto), HttpStatus.OK);
    }
}
