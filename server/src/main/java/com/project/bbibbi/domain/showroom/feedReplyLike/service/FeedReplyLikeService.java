package com.project.bbibbi.domain.showroom.feedReplyLike.service;

import com.project.bbibbi.domain.showroom.feedReplyLike.entity.FeedReplyLike;
import com.project.bbibbi.domain.showroom.feedReplyLike.repository.FeedReplyLikeRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class FeedReplyLikeService {

    private final FeedReplyLikeRepository feedReplyLikeRepository;

    public FeedReplyLikeService(FeedReplyLikeRepository feedReplyLikeRepository) {
        this.feedReplyLikeRepository = feedReplyLikeRepository;
    }
    public FeedReplyLike settingFeedReplyLike(FeedReplyLike feedReplyLike) {
        Integer existCount =
                feedReplyLikeRepository.existCount(feedReplyLike.getFeedReply().getFeedReplyId(), feedReplyLike.getMember().getMemberId());

        if (existCount == 0) {
            feedReplyLikeRepository.save(feedReplyLike);
        } else {
            feedReplyLikeRepository.deleteByFeedReplyIdAndMemberId(
                    feedReplyLike.getFeedReply().getFeedReplyId(), feedReplyLike.getMember().getMemberId());
        }

        Integer updatedLikeCount
                = feedReplyLikeRepository.existCount
                (feedReplyLike.getFeedReply().getFeedReplyId(), feedReplyLike.getMember().getMemberId());

        FeedReplyLike updatedFeedReplyLike = new FeedReplyLike();
        updatedFeedReplyLike.setFeedReply(feedReplyLike.getFeedReply());
        updatedFeedReplyLike.setMember(feedReplyLike.getMember());

        if (updatedLikeCount == 0) {
            updatedFeedReplyLike.setReplyLikeYn(false);
        } else {
            updatedFeedReplyLike.setReplyLikeYn(true);
        }

        updatedFeedReplyLike.setLikeCount(feedReplyLikeRepository.feedReplyLikeCount(
                feedReplyLike.getFeedReply().getFeedReplyId()));

        return updatedFeedReplyLike;
    }

}
