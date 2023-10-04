package com.project.bbibbi.domain.showroom.feedReply.repository;

import com.project.bbibbi.domain.showroom.feedReply.entity.FeedReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedReplyRepository extends JpaRepository<FeedReply, Long> {
    Page<FeedReply> findAllByFeedFeedId(Long feedId, Pageable pageable);
}

