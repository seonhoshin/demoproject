package com.project.bbibbi.domain.showroom.feedComment.service;

import com.project.bbibbi.domain.showroom.feedComment.dto.FeedCommentDto;
import com.project.bbibbi.domain.showroom.feedComment.exception.FeedCommentNotFoundException;
import com.project.bbibbi.domain.showroom.feedComment.repository.FeedCommentRepository;
import com.project.bbibbi.domain.showroom.feedComment.entity.FeedComment;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class FeedCommentService {

    private final FeedCommentRepository feedCommentRepository;
    private final MemberRepository memberRepository;
    public FeedComment saveComment(FeedComment feedComment) {

        FeedComment insertFeedComment= feedCommentRepository.save(feedComment);

        Optional<Member> optionalMember = memberRepository.findById(feedComment.getMember().getMemberId());

        Member member = optionalMember.orElseThrow(() -> {throw new RuntimeException() ; });

        insertFeedComment.setMember(Member.builder().memberId
                (feedComment.getMember().getMemberId()).nickname
                (member.getNickname()).profileImg(member.getProfileImg()).build());

        return insertFeedComment;


    }

    public Optional<FeedComment> findById(Long commentId) {
        return feedCommentRepository.findById(commentId);
    }

    public FeedComment updateComment(Long commentId, FeedCommentDto dto) {
        Optional<FeedComment> optionalFeedComment = feedCommentRepository.findById(commentId);

        if (optionalFeedComment.isPresent()) {
            FeedComment feedComment = optionalFeedComment.get();

            feedComment.setContent(dto.getContent());

            FeedComment updateComment = feedCommentRepository.save(feedComment);
            return updateComment;
        } else {
            throw new FeedCommentNotFoundException("답글을 찾을 수 없습니다. ID: " + commentId);
        }
    }

    public void deleteComment(Long commentId) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new FeedCommentNotFoundException("댓글이 존재하지 않습니다."));

        feedCommentRepository.deleteById(commentId);
    }

}
