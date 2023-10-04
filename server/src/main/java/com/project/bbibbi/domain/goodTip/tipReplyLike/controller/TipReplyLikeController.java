package com.project.bbibbi.domain.goodTip.tipReplyLike.controller;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.goodTip.tipReplyLike.mapper.TipReplyLikeMapper;
import com.project.bbibbi.domain.goodTip.tipReplyLike.service.TipReplyLikeService;
import com.project.bbibbi.domain.goodTip.tipReplyLike.dto.TipReplyLikeRequestDto;
import com.project.bbibbi.domain.goodTip.tipReplyLike.dto.TipReplyLikeResponseDto;
import com.project.bbibbi.domain.goodTip.tipReplyLike.entity.TipReplyLike;
import com.project.bbibbi.global.response.SingleResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tip/{tip-id}/tipreply/{reply-id}/tipreplylike")
@Validated
public class TipReplyLikeController {

    private final static String TIP_REPLY_LIKE_DEFAULT_URL = "/tipreplylike";

    private final TipReplyLikeService tipReplyLikeService;

    private final TipReplyLikeMapper mapper;

    public TipReplyLikeController(TipReplyLikeService tipReplyLikeService, TipReplyLikeMapper mapper) {
        this.tipReplyLikeService = tipReplyLikeService;
        this.mapper = mapper;
    }

    @PatchMapping
    public ResponseEntity patchTipReplyLike(
            @PathVariable("reply-id") Long replyId) {

        Long memberId = loginUtils.getLoginId();

        TipReplyLikeRequestDto requestBody = new TipReplyLikeRequestDto();

        requestBody.setMemberId(memberId);
        requestBody.setTipReplyId(replyId);

        TipReplyLike tipReplyLike = mapper.tipReplyLikeRequestDtoToTipReplyLike(requestBody);

        TipReplyLike updatedTipReplyLike = tipReplyLikeService.settingTipReplyLike(tipReplyLike);

        TipReplyLikeResponseDto tipReplyLikeResponseDto = mapper.tipReplyLikeToTipReplyLikeResponseDto(updatedTipReplyLike);

        return new ResponseEntity<>(new SingleResponseDto<>(tipReplyLikeResponseDto), HttpStatus.OK);
    }
}
