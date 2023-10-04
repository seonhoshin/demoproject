package com.project.bbibbi.domain.goodTip.tipReply.controller;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.goodTip.tipReply.dto.TipReplyRequestDto;
import com.project.bbibbi.domain.goodTip.tipReply.dto.TipReplyResponseDto;
import com.project.bbibbi.domain.goodTip.tipReply.entity.TipReply;
import com.project.bbibbi.domain.goodTip.tipReply.mapper.TipReplyMapper;
import com.project.bbibbi.domain.goodTip.tipReply.service.TipReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tip/{tip-id}/tipreply")
@RequiredArgsConstructor
public class TipReplyController {

    private final static String TIP_REPLY_DEFAULT_URL = "/tipReply";

    private final TipReplyService tipReplyService;
    private final TipReplyMapper tipReplyMapper;

    @PostMapping
    public ResponseEntity tipSave(@PathVariable("tip-id") Long tipId,
                                   @RequestBody TipReplyRequestDto dto)
    {

        dto.setTipId(tipId);
        dto.setMemberId(loginUtils.getLoginId());

        TipReply tipReply = tipReplyMapper.tipReplyRequestDtoToTipReply(dto);

        TipReply savedReply = tipReplyService.replySave(tipReply);

        TipReplyResponseDto tipReplyResponseDto = tipReplyMapper.tipReplyToTipReplyResponseDto(savedReply);

        return ResponseEntity.ok(tipReplyResponseDto);
    }

    @GetMapping("/{reply-id}")
    public ResponseEntity<TipReplyResponseDto> findReply(
            @PathVariable("reply-id") Long replyId) {
        TipReplyResponseDto replyResponseDto = tipReplyService.findReply(replyId);
        return ResponseEntity.ok(replyResponseDto);
    }

    @PatchMapping("/{reply-id}")
    public ResponseEntity<TipReplyResponseDto> updateTipReply(
            @PathVariable("reply-id") Long replyId,
            @RequestBody TipReplyRequestDto dto) {

            TipReply updatedReply = tipReplyService.updateReply(replyId, dto);

            TipReplyResponseDto tipReplyResponseDto = tipReplyMapper.tipReplyToTipReplyResponseDto(updatedReply);

            return ResponseEntity.ok(tipReplyResponseDto);
    }

    @DeleteMapping("/{reply-id}")
    public ResponseEntity<String> deleteTipReply(
            @PathVariable("reply-id") Long replyId) {
        tipReplyService.deleteReply(replyId);

        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
    }


    @GetMapping("/replies")
    public ResponseEntity<List<TipReplyResponseDto>> getAllReplyForTip(
            @PathVariable("tip-id") Long tipId,
            @RequestParam int page) {

        int size = 5;
        Page<TipReply> pageFeedReply = tipReplyService.getAllReplyForTip(tipId, page - 1, size);

        List<TipReply> replyList = pageFeedReply.getContent();

        List<TipReplyResponseDto> replyDtoList = replyList.stream()
                .map(tipReplyMapper::tipReplyToTipReplyResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(replyDtoList);
    }

}
