package com.project.bbibbi.domain.showroom.feedBookmark.repository;

import com.project.bbibbi.domain.showroom.feedBookmark.entity.FeedBookMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedBookMarkRepository extends JpaRepository<FeedBookMark, Long> {

    @Modifying
    @Query(value = "delete from feed_book_mark where feed_id = :feedId and member_id = :memberId ", nativeQuery = true)
    void deleteByFeedIdAndMemberId(@Param("feedId") Long feedId, @Param("memberId") Long memberId);

    @Query(value = "select count(*) from feed_book_mark where feed_id = :feedId and member_id = :memberId ", nativeQuery = true)
    Integer existCount(@Param("feedId") Long feedId, @Param("memberId") Long memberId);

    @Query(value = "select count(*) from feed_book_mark where feed_id = :feedId", nativeQuery = true)
    Integer feedBookMarkCount(@Param("feedId") Long feedId);

    @Modifying
    @Query(value = "delete from feed_book_mark where feed_id = :feedId ", nativeQuery = true)
    void deleteByFeedId(@Param("feedId") Long feedId);


}
