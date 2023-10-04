package com.project.bbibbi.domain.goodTip.tipComment.mapper;

import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import com.project.bbibbi.domain.goodTip.tipComment.dto.TipCommentDto;
import com.project.bbibbi.domain.goodTip.tipComment.entity.TipComment;
import com.project.bbibbi.domain.goodTip.tipReply.entity.TipReply;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface TipCommentMapper {

    default TipComment tipCommentDtoToTipComment(TipCommentDto tipCommentDto){
        if(tipCommentDto == null){
            return null;
        }

        TipComment tipComment = new TipComment();

        Tip tip = new Tip();
        tip.setTipId(tipCommentDto.getTipId());
        tipComment.setTip(tip);

        TipReply tipReply = new TipReply();
        tipReply.setTipReplyId(tipCommentDto.getTipReplyId());
        tipComment.setTipReply(tipReply);

        tipComment.setMember(Member.builder().memberId(tipCommentDto.getMemberId()).build());

        tipComment.setContent(tipCommentDto.getContent());
        tipComment.setParentComment(tipCommentDto.getParentComment());
        tipComment.setCreatedDateTime(LocalDateTime.now());

        return tipComment;
    }

    default TipCommentDto tipCommentToTipCommentDto(TipComment tipComment){
        if(tipComment == null){
            return null;
        }

        TipCommentDto tipCommentDto = new TipCommentDto();

        tipCommentDto.setTipCommentId(tipComment.getTipCommentId());
        tipCommentDto.setContent(tipComment.getContent());
        tipCommentDto.setTipReplyId(tipComment.getTipReply().getTipReplyId());
        tipCommentDto.setTipId(tipComment.getTip().getTipId());
        tipCommentDto.setMemberId(tipComment.getMember().getMemberId());
        tipCommentDto.setParentComment(tipComment.getParentComment());
        tipCommentDto.setNickname(tipComment.getNickname());
        tipCommentDto.setMemberImage(tipComment.getMember().getProfileImg());
        tipCommentDto.setCreatedDateTime(tipComment.getCreatedDateTime());

        return tipCommentDto;

    }
}
