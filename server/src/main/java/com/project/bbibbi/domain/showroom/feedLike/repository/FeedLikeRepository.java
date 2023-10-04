package com.project.bbibbi.domain.showroom.feedLike.repository;

import com.project.bbibbi.domain.showroom.feedLike.entity.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    @Modifying
    @Query(value = "delete from feed_like where feed_id = :feedId and member_id = :memberId ", nativeQuery = true)
    void deleteByFeedIdAndMemberId(@Param("feedId") Long feedId, @Param("memberId") Long memberId);

    @Query(value = "select count(*) from feed_like where feed_id = :feedId and member_id = :memberId ", nativeQuery = true)
    Integer existCount(@Param("feedId") Long feedId, @Param("memberId") Long memberId);

    @Query(value = "select count(*) from feed_like where feed_id = :feedId", nativeQuery = true)
    Integer feedLikeCount(@Param("feedId") Long feedId);

    @Modifying
    @Query(value = "delete from feed_like where feed_id = :feedId", nativeQuery = true)
    void deleteByFeedId(@Param("feedId") Long feedId);

}
