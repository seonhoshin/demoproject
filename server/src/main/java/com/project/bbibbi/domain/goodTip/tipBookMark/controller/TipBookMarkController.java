package com.project.bbibbi.domain.goodTip.tipBookMark.controller;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.goodTip.tipBookMark.dto.TipBookMarkRequestDto;
import com.project.bbibbi.domain.goodTip.tipBookMark.dto.TipBookMarkResponseDto;
import com.project.bbibbi.domain.goodTip.tipBookMark.entity.TipBookMark;
import com.project.bbibbi.domain.goodTip.tipBookMark.mapper.TipBookMarkMapper;
import com.project.bbibbi.domain.goodTip.tipBookMark.service.TipBookMarkService;
import com.project.bbibbi.global.response.SingleResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tip/{tip-id}/tipBookMark")
@Validated
public class TipBookMarkController {

    private final static String TIP_BOOK_MARK_DEFAULT_URL = "/tipBookMark";

    private final TipBookMarkService tipBookmarkService;

    private final TipBookMarkMapper mapper;

    public TipBookMarkController(TipBookMarkService tipBookmarkService, TipBookMarkMapper mapper) {
        this.tipBookmarkService = tipBookmarkService;
        this.mapper = mapper;
    }

    @PatchMapping
    public ResponseEntity patchTipBookMark(@PathVariable("tip-id") Long tipId){

        TipBookMarkRequestDto requestBody = new TipBookMarkRequestDto();

        requestBody.setMemberId(loginUtils.getLoginId());
        requestBody.setTipId(tipId);

        TipBookMark tipBookmark = mapper.tipBookmarkRequestDtoToTipBookmark(requestBody);

        TipBookMark updatedTipBookmark = tipBookmarkService.settingTipBookmark(tipBookmark);

        TipBookMarkResponseDto tipBookmarkResponseDto = mapper.tipBookmarkToTipBookmarkResponseDto(updatedTipBookmark);

        return new ResponseEntity<>(new SingleResponseDto<>(tipBookmarkResponseDto), HttpStatus.OK);
    }
}
