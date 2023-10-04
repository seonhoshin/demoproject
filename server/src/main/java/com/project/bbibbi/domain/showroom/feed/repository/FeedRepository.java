package com.project.bbibbi.domain.showroom.feed.repository;

import com.project.bbibbi.domain.showroom.feed.entity.Feed;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.global.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    List<Feed> findByMemberOrderByCreatedDateTimeDesc(Member member);

    @Query(value = "select * from feed where feed_id in " +
            "(select a.feed_id from feed_like a where a.member_id = :memberId )" +
            " order by created_date_time desc ", nativeQuery = true)
    List<Feed> findByMemberLike(@Param("memberId") long memberId);

    @Query(value = "select * from feed where feed_id in " +
            "(select a.feed_id from feed_book_mark a where a.member_id = :memberId ) " +
            "order by created_date_time desc ", nativeQuery = true)
    List<Feed> findByMemberBookMark(@Param("memberId") long memberId);

    Page<Feed> findByLocation(Location searchcode, Pageable pageable);

    Page<Feed> findByRoomCount(RoomCount searchcode, Pageable pageable);

    Page<Feed> findByRoomInfo(RoomInfo searchcode, Pageable pageable);

    Page<Feed> findByRoomSize(RoomSize searchcode, Pageable pageable);

    Page<Feed> findByRoomType(RoomType roomType, Pageable pageable);

    Page<Feed> findByOrderByCreatedDateTimeDesc(Pageable pageable);

    @Query(value = "select feed.* from (select b.feed_id, row_number() over(order by b.created_date_time desc) as row_num " +
            "from (select cf.* from (select p.*, TRIM(BOTH ' ' FROM REGEXP_REPLACE(p.content, '\\\\<.*?\\\\>', '')) AS clean_content\n" +
            "\t\t\t\t\t\tfrom feed p ) cf where cf.title like %:searchString% or cf.clean_content like %:searchString% ) as b ) as ranked_feed " +
            "inner join (select cf.* from (select p.*, TRIM(BOTH ' ' FROM REGEXP_REPLACE(p.content, '\\\\<.*?\\\\>', '')) AS clean_content\n" +
            "\t\t\t\t\t\tfrom feed p ) cf where cf.title like %:searchString% or cf.clean_content like %:searchString% ) as feed " +
            "on ranked_feed.feed_id = feed.feed_id " +
            "where ranked_feed.row_num > :page * :size " +
            "order by created_date_time desc limit :size ", nativeQuery = true)
    List<Feed> findBySearch(@Param("searchString") String searchString,@Param("page") int page,@Param("size") int size);

    @Query(value = "select count(*)\n" +
            "from ( select p.*, TRIM(BOTH ' ' FROM REGEXP_REPLACE(p.content, '\\\\<.*?\\\\>', '')) AS clean_content\n" +
            "from feed p) pp\n" +
            "where pp.title like %:searchString% or pp.clean_content like %:searchString% ", nativeQuery = true)
    Integer findByCleanSearchCount(@Param("searchString") String searchString);

    @Query(value = "select feed.* from feed " +
            "inner join (select a.feed_id, ( select count(*) from feed_like where feed_id = a.feed_id) as like_count from feed a ) as feed_likecount " +
            "on feed.feed_id = feed_likecount.feed_id " +
            "order by feed_likecount.like_count desc, feed.created_date_time desc ", nativeQuery = true)
    List<Feed> findByOrderByLike();

    Page<Feed> findByOrderByViewsDesc(Pageable pageable);

    @Query(value = "select feed.* from feed " +
            "inner join (select a.feed_id, ( select count(*) from feed_like where feed_id = a.feed_id) as like_count from feed a ) as feed_likecount " +
            "on feed.feed_id = feed_likecount.feed_id " +
            "order by feed_likecount.like_count desc, feed.created_date_time desc limit 10", nativeQuery = true)
    List<Feed> findByLikeTopTen();

}
