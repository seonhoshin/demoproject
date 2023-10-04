package com.project.bbibbi.domain.showroom.feedComment.controller;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.showroom.feedComment.dto.FeedCommentDto;
import com.project.bbibbi.domain.showroom.feedComment.entity.FeedComment;
import com.project.bbibbi.domain.showroom.feedComment.mapper.FeedCommentMapper;
import com.project.bbibbi.domain.showroom.feedComment.service.FeedCommentService;
import com.project.bbibbi.global.response.SingleResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/feed/{feed-id}/feedReply/{feed-reply-id}/feedComment")
public class FeedCommentController {

    private final FeedCommentService feedCommentService;
    private final FeedCommentMapper feedCommentMapper;
    private final static String FEED_COMMENT_DEFAULT_URL = "/feed/{feed-id}/feedReply/{feed-reply-id}/feedComment";

    @Autowired
    public FeedCommentController(FeedCommentService feedCommentService,
                                 FeedCommentMapper feedCommentMapper) {
        this.feedCommentService = feedCommentService;
        this.feedCommentMapper = feedCommentMapper;
    }

    @PostMapping
    public ResponseEntity createComment(
            @PathVariable("feed-id" ) Long feedId,
            @PathVariable("feed-reply-id" ) Long feedReplyId,
            @RequestBody FeedCommentDto dto) {

        dto.setFeedId(feedId);
        dto.setFeedReplyId(feedReplyId);
        dto.setMemberId(loginUtils.getLoginId());

        FeedComment feedComment = feedCommentMapper.feedCommentDtoToFeedComment(dto);

        FeedComment savedComment = feedCommentService.saveComment(feedComment);

        FeedCommentDto responseDto = feedCommentMapper.feedCommentToFeedCommentDto(savedComment);

        URI location = UriComponentsBuilder.newInstance().path(
                FEED_COMMENT_DEFAULT_URL+"/{feed-comment-id}"
        ).buildAndExpand(feedId,feedReplyId,savedComment.getFeedCommentId()).toUri();

        return ResponseEntity.created(location).body(new SingleResponseDto<>(responseDto));

    }

    @GetMapping("/{commentId}")
    public ResponseEntity findComment(@PathVariable Long commentId) {
        Optional<FeedComment> optionalFeedComment = feedCommentService.findById(commentId);
        if (optionalFeedComment.isPresent()) {
            FeedComment feedComment = optionalFeedComment.get();
            return new ResponseEntity(new SingleResponseDto<>(feedComment), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity updateComment(
            @PathVariable Long commentId,
            @RequestBody FeedCommentDto dto) {

            FeedComment updateFeedComment = feedCommentService.updateComment(commentId, dto);

            FeedCommentDto feedCommentDto = feedCommentMapper.feedCommentToFeedCommentDto(updateFeedComment);

            return new ResponseEntity(new SingleResponseDto<>(feedCommentDto), HttpStatus.OK);

    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId) {

        feedCommentService.deleteComment(commentId);

        return new ResponseEntity<>("댓글이 성공적으로 삭제되었습니다.", HttpStatus.NO_CONTENT);
    }

}
