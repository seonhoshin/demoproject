package com.project.bbibbi.domain.goodTip.tipBookmark.mapper;

import com.project.bbibbi.domain.goodTip.tipBookmark.dto.TipBookmarkRequestDto;
import com.project.bbibbi.domain.goodTip.tipBookmark.entity.TipBookmark;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import com.project.bbibbi.domain.goodTip.tipBookmark.dto.TipBookmarkResponseDto;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface TipBookmarkMapper {
    default TipBookmark tipBookmarkRequestDtoToTipBookmark(TipBookmarkRequestDto tipBookmarkRequestDto) {
        if (tipBookmarkRequestDto == null) {
            return null;
        }

        TipBookmark tipBookmark = new TipBookmark();

        Tip tip = new Tip();
        tip.setTipId(tipBookmarkRequestDto.getTipId());
        tipBookmark.setTip(tip);
        tipBookmark.setMember(Member.builder().memberId(tipBookmarkRequestDto.getMemberId()).build());
        tipBookmark.setCreatedDateTime(LocalDateTime.now());

        return tipBookmark;
    }

    default TipBookmarkResponseDto tipBookmarkToTipBookmarkResponseDto(TipBookmark tipBookmark) {
        if (tipBookmark == null) {
            return null;
        }

        TipBookmarkResponseDto tipBookmarkResponseDto = new TipBookmarkResponseDto();

        tipBookmarkResponseDto.setMemberId(tipBookmark.getMember().getMemberId());
        tipBookmarkResponseDto.setTipId(tipBookmark.getTip().getTipId());
        tipBookmarkResponseDto.setBookmarkYn(tipBookmark.getBookmarkYn());
        tipBookmarkResponseDto.setBookmarkCount(tipBookmark.getBookmarkCount());

        return tipBookmarkResponseDto;
    }
}
