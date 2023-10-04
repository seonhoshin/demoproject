package com.project.bbibbi.domain.showroom.feedReply.controller;


import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.showroom.feedReply.dto.FeedReplyRequestDto;
import com.project.bbibbi.domain.showroom.feedReply.dto.FeedReplyResponseDto;
import com.project.bbibbi.domain.showroom.feedReply.entity.FeedReply;
import com.project.bbibbi.domain.showroom.feedReply.mapper.FeedReplyMapper;
import com.project.bbibbi.domain.showroom.feedReply.service.FeedReplyService;
import com.project.bbibbi.global.response.MultiResponseDto;
import com.project.bbibbi.global.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/feed/{feed-id}/feedReply")
@RestController
public class FeedReplyController {
    private final FeedReplyService feedReplyService;
    private final FeedReplyMapper feedReplyMapper;

    private final static String FEED_REPLY_DEFAULT_URL = "/feed/{feed-id}/feedReply";


    @PostMapping
    public ResponseEntity feedSave(@PathVariable ("feed-id")Long feedId,
                                    @RequestBody FeedReplyRequestDto dto)
    {

    dto.setFeedId(feedId);
    dto.setMemberId(loginUtils.getLoginId());

    FeedReply feedReply1 = feedReplyMapper.feedReplyRequestDtoToFeedReply(dto);

    FeedReply savedReply = feedReplyService.replySave(feedReply1);

    FeedReplyResponseDto feedReplyResponseDto = feedReplyMapper.feedReplyToFeedReplyResponseDto(savedReply);

    URI location = UriComponentsBuilder.newInstance().path(FEED_REPLY_DEFAULT_URL+"/{feed-reply-id}")
            .buildAndExpand(feedId,savedReply.getFeedReplyId()).toUri();

    return ResponseEntity.created(location).body(new SingleResponseDto<>(feedReplyResponseDto));

    }

    @GetMapping("/{reply-id}")
    public ResponseEntity findReply(@PathVariable("reply-id") Long replyId) {

        FeedReplyResponseDto replyResponseDto = feedReplyService.findReply(replyId);

        return new ResponseEntity<>(new SingleResponseDto<>(replyResponseDto), HttpStatus.OK);
    }

    @PatchMapping("/{reply-id}")
    public ResponseEntity updateFeedReply(
            @PathVariable("reply-id") Long replyId,
            @RequestBody FeedReplyRequestDto dto) {

        FeedReply upatedReply = feedReplyService.updateReply(replyId, dto);

        FeedReplyResponseDto feedReplyResponseDto = feedReplyMapper.feedReplyToFeedReplyResponseDto(upatedReply);

        return new ResponseEntity(new SingleResponseDto<>(feedReplyResponseDto), HttpStatus.OK);
    }

    @DeleteMapping("/{reply-id}")
    public ResponseEntity deleteFeedReply(@PathVariable("reply-id") Long replyId) {

        feedReplyService.deleteReply(replyId);

        return new ResponseEntity<>("댓글이 성공적으로 삭제되었습니다.", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/replies")
    public ResponseEntity getAllReplyForFeed(
            @PathVariable("feed-id") Long feedId,
            @RequestParam int page) {

        int size = 5;
        Page<FeedReply> pageFeedReply = feedReplyService.getAllReplyForFeed(feedId, page - 1, size);

        List<FeedReply> replyList = pageFeedReply.getContent();

        List<FeedReplyResponseDto> replyDtoList = replyList.stream()
                .map(feedReplyMapper::feedReplyToFeedReplyResponseDto)
                .collect(Collectors.toList());

        return new ResponseEntity(new MultiResponseDto<>(replyDtoList), HttpStatus.OK);
    }

}
