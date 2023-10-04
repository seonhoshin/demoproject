package com.project.bbibbi.domain.showroom.feedComment.controller;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.showroom.feedComment.dto.FeedCommentDto;
import com.project.bbibbi.domain.showroom.feedComment.entity.FeedComment;
import com.project.bbibbi.domain.showroom.feedComment.mapper.FeedCommentMapper;
import com.project.bbibbi.domain.showroom.feedComment.service.FeedCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/feed/{feed-id}/feedReply/{feed-reply-id}/feedComment")
public class FeedCommentController {

    private final FeedCommentService feedCommentService;
    private final FeedCommentMapper feedCommentMapper;

    @Autowired
    public FeedCommentController(FeedCommentService feedCommentService,
                                 FeedCommentMapper feedCommentMapper) {
        this.feedCommentService = feedCommentService;
        this.feedCommentMapper = feedCommentMapper;
    }

    @PostMapping
    public ResponseEntity<FeedCommentDto> createComment(
            @PathVariable("feed-id" ) Long feedId,
            @PathVariable("feed-reply-id" ) Long feedReplyId,
            @RequestBody FeedCommentDto dto) {

        dto.setFeedId(feedId);
        dto.setFeedReplyId(feedReplyId);
        dto.setMemberId(loginUtils.getLoginId());

        FeedComment feedComment = feedCommentMapper.feedCommentDtoToFeedComment(dto);

        FeedComment savedComment = feedCommentService.saveComment(feedComment);

        FeedCommentDto responseDto = feedCommentMapper.feedCommentToFeedCommentDto(savedComment);

        return ResponseEntity.ok(responseDto);

    }

    @GetMapping("/{commentId}")
    public ResponseEntity<FeedCommentDto> findComment(@PathVariable Long commentId) {
        Optional<FeedComment> optionalFeedComment = feedCommentService.findById(commentId);
        if (optionalFeedComment.isPresent()) {
            FeedComment feedComment = optionalFeedComment.get();
            return ResponseEntity.ok(feedCommentMapper.feedCommentToFeedCommentDto(feedComment));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<FeedCommentDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody FeedCommentDto dto) {

            FeedComment updateFeedComment = feedCommentService.updateComment(commentId, dto);

            FeedCommentDto feedCommentDto = feedCommentMapper.feedCommentToFeedCommentDto(updateFeedComment);

            return ResponseEntity.ok(feedCommentDto);

    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        feedCommentService.deleteComment(commentId);
        return ResponseEntity.ok("답글이 성공적으로 삭제되었습니다.");
    }

}
