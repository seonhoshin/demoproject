package com.project.bbibbi.domain.goodTip.tipBookmark.controller;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.goodTip.tipBookmark.dto.TipBookmarkRequestDto;
import com.project.bbibbi.domain.goodTip.tipBookmark.dto.TipBookmarkResponseDto;
import com.project.bbibbi.domain.goodTip.tipBookmark.entity.TipBookmark;
import com.project.bbibbi.domain.goodTip.tipBookmark.mapper.TipBookmarkMapper;
import com.project.bbibbi.domain.goodTip.tipBookmark.service.TipBookmarkService;
import com.project.bbibbi.global.response.SingleResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tip/{tip-id}/tipbookmark")
@Validated
public class TipBookmarkController {

    private final static String TIP_BOOKMARK_DEFAULT_URL = "/tipBookmark";

    private final TipBookmarkService tipBookmarkService;

    private final TipBookmarkMapper mapper;

    public TipBookmarkController(TipBookmarkService tipBookmarkService, TipBookmarkMapper mapper) {
        this.tipBookmarkService = tipBookmarkService;
        this.mapper = mapper;
    }

    @PatchMapping
    public ResponseEntity patchTipBookmark(@PathVariable("tip-id") Long tipId){

        TipBookmarkRequestDto requestBody = new TipBookmarkRequestDto();

        requestBody.setMemberId(loginUtils.getLoginId());
        requestBody.setTipId(tipId);

        TipBookmark tipBookmark = mapper.tipBookmarkRequestDtoToTipBookmark(requestBody);

        TipBookmark updatedTipBookmark = tipBookmarkService.settingTipBookmark(tipBookmark);

        TipBookmarkResponseDto tipBookmarkResponseDto = mapper.tipBookmarkToTipBookmarkResponseDto(updatedTipBookmark);

        return new ResponseEntity<>(new SingleResponseDto<>(tipBookmarkResponseDto), HttpStatus.OK);
    }
}
