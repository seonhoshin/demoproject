package com.project.bbibbi.domain.goodTip.tipComment.controller;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.goodTip.tipComment.dto.TipCommentDto;
import com.project.bbibbi.domain.goodTip.tipComment.entity.TipComment;
import com.project.bbibbi.domain.goodTip.tipComment.mapper.TipCommentMapper;
import com.project.bbibbi.domain.goodTip.tipComment.service.TipCommentService;
import com.project.bbibbi.global.exception.tipexception.TipCommentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/tip/{tip-id}/tipReply/{reply-id}/tipComment")
public class TipCommentController {

    private static final String TIP_COMMENT_DEFAULT_URL = "/tipComment";

    private final TipCommentService tipCommentService;
    private final TipCommentMapper tipCommentMapper;

    @Autowired
    public TipCommentController(TipCommentService tipCommentService,
                                TipCommentMapper tipCommentMapper) {
        this.tipCommentService = tipCommentService;
        this.tipCommentMapper = tipCommentMapper;
    }

    @PostMapping
    public ResponseEntity<TipCommentDto> createComment(
            @PathVariable("tip-id") Long tipId,
            @PathVariable("reply-id") Long tipReplyId,
            @RequestBody TipCommentDto dto) {

        dto.setTipId(tipId);
        dto.setTipReplyId(tipReplyId);
        dto.setMemberId(loginUtils.getLoginId());

        TipComment tipComment = tipCommentMapper.tipCommentDtoToTipComment(dto);

        TipComment savedComment = tipCommentService.saveComment(tipComment);

        TipCommentDto responseDto = tipCommentMapper.tipCommentToTipCommentDto(savedComment);

        return ResponseEntity.ok(responseDto);
    }


    @GetMapping("/{comment-id}")
    public ResponseEntity<TipCommentDto> findComment(@PathVariable("comment-id") Long commentId) {
        Optional<TipComment> optionalTipComment = tipCommentService.findById(commentId);
        if (optionalTipComment.isPresent()) {
            TipComment tipComment = optionalTipComment.get();
            return ResponseEntity.ok(tipCommentMapper.tipCommentToTipCommentDto(tipComment));
        } else {
            throw new TipCommentNotFoundException();
        }
    }

    @PatchMapping("/{comment-id}")
    public ResponseEntity<TipCommentDto> updateComment(
            @PathVariable("comment-id") Long commentId,
            @RequestBody TipCommentDto dto) {

            TipComment updateTipComment = tipCommentService.updateComment(commentId, dto);

            TipCommentDto tipCommentDto = tipCommentMapper.tipCommentToTipCommentDto(updateTipComment);

            return ResponseEntity.ok(tipCommentDto);
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity<String> deleteComment(@PathVariable("comment-id") Long commentId) {
        tipCommentService.deleteComment(commentId);
        return ResponseEntity.ok("답글이 성공적으로 삭제되었습니다.");
    }

}
