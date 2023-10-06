package com.project.bbibbi.domain.goodTip.tipReply.controller;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.goodTip.tipReply.dto.TipReplyRequestDto;
import com.project.bbibbi.domain.goodTip.tipReply.dto.TipReplyResponseDto;
import com.project.bbibbi.domain.goodTip.tipReply.entity.TipReply;
import com.project.bbibbi.domain.goodTip.tipReply.mapper.TipReplyMapper;
import com.project.bbibbi.domain.goodTip.tipReply.service.TipReplyService;
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

@RestController
@RequestMapping("/tip/{tip-id}/tipReply")
@RequiredArgsConstructor
public class TipReplyController {

    private final static String TIP_REPLY_DEFAULT_URL = "/tip/{tip-id}/tipReply";

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

        URI location = UriComponentsBuilder.newInstance().path(TIP_REPLY_DEFAULT_URL+"/{tip-reply-id}")
                .buildAndExpand(tipId, savedReply.getTipReplyId()).toUri();

        return ResponseEntity.created(location).body(new SingleResponseDto<>(tipReplyResponseDto));
    }

    @GetMapping("/{reply-id}")
    public ResponseEntity findReply(
            @PathVariable("reply-id") Long replyId) {

        TipReplyResponseDto replyResponseDto = tipReplyService.findReply(replyId);

        return new ResponseEntity(new SingleResponseDto<>(replyResponseDto), HttpStatus.OK);
    }

    @PatchMapping("/{reply-id}")
    public ResponseEntity updateTipReply(
            @PathVariable("reply-id") Long replyId,
            @RequestBody TipReplyRequestDto dto) {

            TipReply updatedReply = tipReplyService.updateReply(replyId, dto);

            TipReplyResponseDto tipReplyResponseDto = tipReplyMapper.tipReplyToTipReplyResponseDto(updatedReply);

            return new ResponseEntity<>(new SingleResponseDto<>(tipReplyResponseDto), HttpStatus.OK);
    }

    @DeleteMapping("/{reply-id}")
    public ResponseEntity<String> deleteTipReply(
            @PathVariable("reply-id") Long replyId) {
        tipReplyService.deleteReply(replyId);

        return new ResponseEntity<>("댓글이 성공적으로 삭제되었습니다.", HttpStatus.NO_CONTENT);
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

        return new ResponseEntity(new MultiResponseDto<>(replyDtoList), HttpStatus.OK);
    }

}
