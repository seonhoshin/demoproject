package com.project.bbibbi.domain.showroom.feedReply.controller;


import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.showroom.feedReply.dto.FeedReplyRequestDto;
import com.project.bbibbi.domain.showroom.feedReply.dto.FeedReplyResponseDto;
import com.project.bbibbi.domain.showroom.feedReply.entity.FeedReply;
import com.project.bbibbi.domain.showroom.feedReply.mapper.FeedReplyMapper;
import com.project.bbibbi.domain.showroom.feedReply.service.FeedReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/feed/{feed-id}/feedReply")
@RestController
public class FeedReplyController {
    private final FeedReplyService feedReplyService;
    private final FeedReplyMapper feedReplyMapper;


    @PostMapping
    public ResponseEntity feedSave(@PathVariable ("feed-id")Long feedId,
                                    @RequestBody FeedReplyRequestDto dto)
    {

    dto.setFeedId(feedId);
    dto.setMemberId(loginUtils.getLoginId());

    FeedReply feedReply1 = feedReplyMapper.feedReplyRequestDtoToFeedReply(dto);

    FeedReply savedReply = feedReplyService.replySave(feedReply1);

    FeedReplyResponseDto feedReplyResponseDto = feedReplyMapper.feedReplyToFeedReplyResponseDto(savedReply);

    return ResponseEntity.ok(feedReplyResponseDto);

    }

    @GetMapping("/{replyId}")
    public ResponseEntity<FeedReplyResponseDto> findReply(@PathVariable Long replyId) {
        FeedReplyResponseDto replyResponseDto = feedReplyService.findReply(replyId);
        return ResponseEntity.ok(replyResponseDto);
    }

    @PatchMapping("/{reply-id}")
    public ResponseEntity<FeedReplyResponseDto> updateFeedReply(
            @PathVariable("reply-id") Long replyId,
            @RequestBody FeedReplyRequestDto dto) {

        FeedReply upatedReply = feedReplyService.updateReply(replyId, dto);

        FeedReplyResponseDto feedReplyResponseDto = feedReplyMapper.feedReplyToFeedReplyResponseDto(upatedReply);

        return ResponseEntity.ok(feedReplyResponseDto);
    }
    @DeleteMapping("/{replyId}")
    public ResponseEntity<String> deleteFeedReply(@PathVariable Long replyId) {

        feedReplyService.deleteReply(replyId);

        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/replies")
    public ResponseEntity<List<FeedReplyResponseDto>> getAllReplyForFeed(
            @PathVariable("feed-id") Long feedId,
            @RequestParam int page) {

        int size = 5;
        Page<FeedReply> pageFeedReply = feedReplyService.getAllReplyForFeed(feedId, page - 1, size);

        List<FeedReply> replyList = pageFeedReply.getContent();

        List<FeedReplyResponseDto> replyDtoList = replyList.stream()
                .map(feedReplyMapper::feedReplyToFeedReplyResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(replyDtoList);
    }

}
