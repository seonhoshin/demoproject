package com.project.bbibbi.domain.goodTip.tipReplyLike.mapper;

import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.goodTip.tipReply.entity.TipReply;
import com.project.bbibbi.domain.goodTip.tipReplyLike.dto.TipReplyLikeRequestDto;
import com.project.bbibbi.domain.goodTip.tipReplyLike.dto.TipReplyLikeResponseDto;
import com.project.bbibbi.domain.goodTip.tipReplyLike.entity.TipReplyLike;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface TipReplyLikeMapper {
    default TipReplyLike tipReplyLikeRequestDtoToTipReplyLike(TipReplyLikeRequestDto tipReplyLikeRequestDto){
        if(tipReplyLikeRequestDto == null){
            return null;
        }

        TipReplyLike tipReplyLike = new TipReplyLike();

        TipReply tipReply = new TipReply();
        tipReply.setTipReplyId(tipReplyLikeRequestDto.getTipReplyId());
        tipReplyLike.setTipReply(tipReply);
        tipReplyLike.setMember(Member.builder().memberId(tipReplyLikeRequestDto.getMemberId()).build());
        tipReplyLike.setCreatedDateTime(LocalDateTime.now());

        return tipReplyLike;
    }

    default TipReplyLikeResponseDto tipReplyLikeToTipReplyLikeResponseDto(TipReplyLike tipReplyLike){
        if(tipReplyLike == null){
            return null;
        }

        TipReplyLikeResponseDto tipReplyLikeResponseDto = new TipReplyLikeResponseDto();

        tipReplyLikeResponseDto.setMemberId(tipReplyLike.getMember().getMemberId());
        tipReplyLikeResponseDto.setTipReplyId(tipReplyLike.getTipReply().getTipReplyId());
        tipReplyLikeResponseDto.setReplyLikeYn(tipReplyLike.getReplyLikeYn());
        tipReplyLikeResponseDto.setLikeCount(tipReplyLike.getLikeCount());

        return tipReplyLikeResponseDto;
    }
}
