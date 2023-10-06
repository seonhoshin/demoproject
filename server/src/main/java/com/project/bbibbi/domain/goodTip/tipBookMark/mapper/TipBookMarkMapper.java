package com.project.bbibbi.domain.goodTip.tipBookMark.mapper;

import com.project.bbibbi.domain.goodTip.tipBookMark.dto.TipBookMarkRequestDto;
import com.project.bbibbi.domain.goodTip.tipBookMark.entity.TipBookMark;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import com.project.bbibbi.domain.goodTip.tipBookMark.dto.TipBookMarkResponseDto;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface TipBookMarkMapper {
    default TipBookMark tipBookmarkRequestDtoToTipBookmark(TipBookMarkRequestDto tipBookmarkRequestDto) {
        if (tipBookmarkRequestDto == null) {
            return null;
        }

        TipBookMark tipBookmark = new TipBookMark();

        Tip tip = new Tip();
        tip.setTipId(tipBookmarkRequestDto.getTipId());
        tipBookmark.setTip(tip);
        tipBookmark.setMember(Member.builder().memberId(tipBookmarkRequestDto.getMemberId()).build());
        tipBookmark.setCreatedDateTime(LocalDateTime.now());

        return tipBookmark;
    }

    default TipBookMarkResponseDto tipBookmarkToTipBookmarkResponseDto(TipBookMark tipBookmark) {
        if (tipBookmark == null) {
            return null;
        }

        TipBookMarkResponseDto tipBookmarkResponseDto = new TipBookMarkResponseDto();

        tipBookmarkResponseDto.setMemberId(tipBookmark.getMember().getMemberId());
        tipBookmarkResponseDto.setTipId(tipBookmark.getTip().getTipId());
        tipBookmarkResponseDto.setBookmarkYn(tipBookmark.getBookmarkYn());
        tipBookmarkResponseDto.setBookmarkCount(tipBookmark.getBookmarkCount());

        return tipBookmarkResponseDto;
    }
}
