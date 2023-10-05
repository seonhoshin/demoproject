package com.project.bbibbi.domain.showroom.feedBookMark.controller;


import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.showroom.feedBookMark.dto.FeedBookMarkRequestDto;
import com.project.bbibbi.domain.showroom.feedBookMark.dto.FeedBookMarkResponseDto;
import com.project.bbibbi.domain.showroom.feedBookMark.entity.FeedBookMark;
import com.project.bbibbi.domain.showroom.feedBookMark.mapper.FeedBookMarkMapper;
import com.project.bbibbi.domain.showroom.feedBookMark.service.FeedBookMarkService;
import com.project.bbibbi.global.response.SingleResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feed/{feed-id}/feedBookMark")
@Validated


public class FeedBookMarkController {

    private final static String FEED_bookMark_DEFAULT_URL = "/feed/{feed-id}/feedBookMark";

    private final FeedBookMarkService feedBookMarkService;

    private final FeedBookMarkMapper mapper;

    public FeedBookMarkController(FeedBookMarkService feedBookMarkService, FeedBookMarkMapper mapper) {
        this.feedBookMarkService = feedBookMarkService;
        this.mapper = mapper;
    }

    @PatchMapping
    public ResponseEntity patchFeedBookMark(@PathVariable("feed-id") Long feedId){

        Long memberId = loginUtils.getLoginId();

        FeedBookMarkRequestDto requestBody
                = new FeedBookMarkRequestDto();

        requestBody.setMemberId(memberId);
        requestBody.setFeedId(feedId);

        FeedBookMark feedBookMark = mapper.feedBookMarkRequestDtoToFeedBookMark(requestBody);

        FeedBookMark updatedFeedBookMark = feedBookMarkService.settingFeedBookMark(feedBookMark);

        FeedBookMarkResponseDto feedBookMarkResponseDto = mapper.feedBookMarkToFeedBookMarkResponseDto(updatedFeedBookMark);

        return new ResponseEntity<>(new SingleResponseDto<>(feedBookMarkResponseDto), HttpStatus.OK);
    }

}
