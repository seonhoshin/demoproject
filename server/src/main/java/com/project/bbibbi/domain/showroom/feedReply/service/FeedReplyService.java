package com.project.bbibbi.domain.showroom.feedReply.service;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.showroom.feedReply.FeedReplyNotFoundException.FeedReplyNotFoundException;
import com.project.bbibbi.domain.showroom.feedReply.dto.FeedReplyRequestDto;
import com.project.bbibbi.domain.showroom.feedReply.dto.FeedReplyResponseDto;
import com.project.bbibbi.domain.showroom.feedReply.entity.FeedReply;
import com.project.bbibbi.domain.showroom.feedReply.mapper.FeedReplyMapper;
import com.project.bbibbi.domain.showroom.feedReply.repository.FeedReplyRepository;
import com.project.bbibbi.domain.showroom.feedReplyLike.repository.FeedReplyLikeRepository;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class FeedReplyService {

    private final FeedReplyRepository feedReplyRepository;

    private final FeedReplyLikeRepository feedReplyLikeRepository;
    private final MemberRepository memberRepository;
    private final FeedReplyMapper feedReplyMapper;


    public FeedReply replySave(FeedReply feedReply) {

            FeedReply insertFeedReply = feedReplyRepository.save(feedReply);

            Optional<Member> optionalMember = memberRepository.findById(feedReply.getMember().getMemberId());

            Member member = optionalMember.orElseThrow(() -> {throw new RuntimeException() ; });

            insertFeedReply.setMember(Member.builder().memberId
                    (feedReply.getMember().getMemberId()).nickname
                    (member.getNickname()).profileImg(member.getProfileImg()).build());

            return insertFeedReply;

        }

    public FeedReplyResponseDto findReply(Long replyId) {

        FeedReply reply = feedReplyRepository.findById(replyId)
                .orElseThrow(() -> new FeedReplyNotFoundException("댓글이 존재하지 않습니다."));

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        if(member == null){
            reply.setReplyLikeYn(false);
        }
        else {
            int loginUserLikeYn = feedReplyLikeRepository.existCount(reply.getFeedReplyId(), member.getMemberId());
            if(loginUserLikeYn == 0)
                reply.setReplyLikeYn(false);
            else reply.setReplyLikeYn(true);
        }

        return feedReplyMapper.feedReplyToFeedReplyResponseDto(reply);
    }
    public Optional<FeedReply> findById(Long replyId) {
        return feedReplyRepository.findById(replyId);
    }


    public FeedReply updateReply(Long replyId, FeedReplyRequestDto dto) {
        Optional<FeedReply> optionalFeedReply = feedReplyRepository.findById(replyId);

        if (optionalFeedReply.isPresent()) {
            FeedReply feedReply = optionalFeedReply.get();

            feedReply.setContent(dto.getContent());

            feedReply.setReplyLikeYn(dto.getReplyLikeYn());

            FeedReply updatedReply = feedReplyRepository.save(feedReply);
            return updatedReply;
        } else {
            throw new FeedReplyNotFoundException("댓글을 찾을 수 없습니다. ID: " + replyId);
        }
    }

    public void deleteReply (Long replyId){
            FeedReply reply = feedReplyRepository.findById(replyId)
                    .orElseThrow(() -> new FeedReplyNotFoundException("댓글이 존재하지 않습니다."));

            feedReplyLikeRepository.deleteByFeedReplyId(replyId);

            feedReplyRepository.deleteById(replyId);
    }
    public Page<FeedReply> getAllReplyForFeed(Long feedId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return feedReplyRepository.findAllByFeedFeedId(feedId, pageable);
    }

}

