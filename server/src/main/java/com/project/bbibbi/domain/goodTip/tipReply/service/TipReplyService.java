package com.project.bbibbi.domain.goodTip.tipReply.service;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.goodTip.tipReply.dto.TipReplyRequestDto;
import com.project.bbibbi.domain.goodTip.tipReply.entity.TipReply;
import com.project.bbibbi.domain.goodTip.tipReply.mapper.TipReplyMapper;
import com.project.bbibbi.domain.goodTip.tipReply.repository.TipReplyRepository;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.member.repository.MemberRepository;
import com.project.bbibbi.domain.goodTip.tipReply.dto.TipReplyResponseDto;
import com.project.bbibbi.domain.goodTip.tipReplyLike.repository.TipReplyLikeRepository;
import com.project.bbibbi.global.exception.tipexception.TipReplyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TipReplyService {

    private final TipReplyRepository tipReplyRepository;

    private final TipReplyLikeRepository tipReplyLikeRepository;

    private final MemberRepository memberRepository;

    private final TipReplyMapper tipReplyMapper;

    public TipReply replySave(TipReply tipReply) {
        TipReply insertTipReply = tipReplyRepository.save(tipReply);

        Optional<Member> optionalMember = memberRepository.findById(tipReply.getMember().getMemberId());

        Member member = optionalMember.orElseThrow(() -> {throw new RuntimeException() ; });

        insertTipReply.setMember(Member.builder().memberId
                (tipReply.getMember().getMemberId()).nickname
                (member.getNickname()).profileImg(member.getProfileImg()).build());

        return insertTipReply;
    }

    public TipReplyResponseDto findReply(Long replyId) {
        TipReply reply = tipReplyRepository.findById(replyId)
                .orElseThrow(TipReplyNotFoundException::new);

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        if(member == null){
            reply.setReplyLikeYn(false);
        }
        else {
            int loginUserLikeYn = tipReplyLikeRepository.existCount(reply.getTipReplyId(), member.getMemberId());
            if(loginUserLikeYn == 0)
                reply.setReplyLikeYn(false);
            else reply.setReplyLikeYn(true);
        }

        return tipReplyMapper.tipReplyToTipReplyResponseDto(reply);
    }

    public Optional<TipReply> findById(Long replyId) {
        return tipReplyRepository.findById(replyId);
    }

    public TipReply updateReply(Long replyId, TipReplyRequestDto dto) {
        Optional<TipReply> optionalTipReply = tipReplyRepository.findById(replyId);

        if (optionalTipReply.isPresent()) {
            TipReply tipReply = optionalTipReply.get();

            tipReply.setContent(dto.getContent());

            tipReply.setReplyLikeYn(dto.getReplyLikeYn());

            TipReply updatedReply = tipReplyRepository.save(tipReply);
            return updatedReply;
        } else {
            throw new TipReplyNotFoundException();
        }
    }

    public void deleteReply(Long replyId) {
        TipReply reply = tipReplyRepository.findById(replyId)
                .orElseThrow(TipReplyNotFoundException::new);

        tipReplyLikeRepository.deleteByTipReplyId(replyId);

        tipReplyRepository.deleteById(replyId);
    }

    public Page<TipReply> getAllReplyForTip(Long tipId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return tipReplyRepository.findAllByTip_TipId(tipId, pageable);
    }
}
