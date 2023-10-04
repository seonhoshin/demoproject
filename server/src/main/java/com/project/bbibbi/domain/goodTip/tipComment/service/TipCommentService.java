package com.project.bbibbi.domain.goodTip.tipComment.service;

import com.project.bbibbi.domain.goodTip.tipComment.repository.TipCommentRepository;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.member.repository.MemberRepository;
import com.project.bbibbi.domain.goodTip.tipComment.dto.TipCommentDto;
import com.project.bbibbi.domain.goodTip.tipComment.entity.TipComment;
import com.project.bbibbi.global.exception.tipexception.TipCommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class TipCommentService {

    private final TipCommentRepository tipCommentRepository;
    private final MemberRepository memberRepository;
    public TipComment saveComment(TipComment tipComment) {

        TipComment insertTipComment= tipCommentRepository.save(tipComment);

        Optional<Member> optionalMember = memberRepository.findById(tipComment.getMember().getMemberId());

        Member member = optionalMember.orElseThrow(() -> {throw new RuntimeException() ; });

        insertTipComment.setMember(Member.builder().memberId
                (tipComment.getMember().getMemberId()).nickname
                (member.getNickname()).profileImg(member.getProfileImg()).build());

        return insertTipComment;

    }

    public Optional<TipComment> findById(Long commentId) {
        return tipCommentRepository.findById(commentId);
    }

    public TipComment updateComment(Long commentId, TipCommentDto dto) {
        Optional<TipComment> optionalTipComment = tipCommentRepository.findById(commentId);

        if (optionalTipComment.isPresent()) {
            TipComment tipComment = optionalTipComment.get();

            tipComment.setContent(dto.getContent());
            tipComment.setModifiedDateTime(LocalDateTime.now());

            TipComment updateComment = tipCommentRepository.save(tipComment);
            return updateComment;
        } else {
            throw new TipCommentNotFoundException();
        }
    }


    public void deleteComment(Long commentId) {
        TipComment comment = tipCommentRepository.findById(commentId)
                .orElseThrow(TipCommentNotFoundException::new);

        tipCommentRepository.deleteById(commentId);
    }

}
