package com.project.bbibbi.domain.goodTip.tipReply.mapper;

import com.project.bbibbi.domain.goodTip.tipReply.dto.TipReplyRequestDto;
import com.project.bbibbi.domain.goodTip.tipReply.entity.TipReply;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import com.project.bbibbi.domain.goodTip.tipReply.dto.TipReplyResponseDto;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface TipReplyMapper {

    default TipReply tipReplyRequestDtoToTipReply(TipReplyRequestDto tipReplyRequestDto){
        if(tipReplyRequestDto == null){
            return null;
        }

        TipReply tipReply = new TipReply();

        tipReply.setMember(Member.builder().memberId(tipReplyRequestDto.getMemberId()).build());

        Tip tip = new Tip();
        tip.setTipId(tipReplyRequestDto.getTipId());
        tipReply.setTip(tip);

        tipReply.setContent(tipReplyRequestDto.getContent());
        tipReply.setCreatedDateTime(LocalDateTime.now());

        return tipReply;

    }

    default TipReplyResponseDto tipReplyToTipReplyResponseDto(TipReply tipReply){
        if(tipReply == null){
            return null;
        }

        TipReplyResponseDto tipReplyResponseDto = new TipReplyResponseDto();
        tipReplyResponseDto.setTipReplyId(tipReply.getTipReplyId());
        tipReplyResponseDto.setContent(tipReply.getContent());
        tipReplyResponseDto.setTipId(tipReply.getTip().getTipId());
        tipReplyResponseDto.setMemberId(tipReply.getMember().getMemberId());
        tipReplyResponseDto.setNickname(tipReply.getMember().getNickname());
        tipReplyResponseDto.setMemberImage(tipReply.getMember().getProfileImg());
        tipReplyResponseDto.setCreatedDateTime(tipReply.getCreatedDateTime());

        return tipReplyResponseDto;
    }
}
