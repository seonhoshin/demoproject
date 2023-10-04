package com.project.bbibbi.domain.goodTip.tip.repository;

import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipRepository extends JpaRepository<Tip, Long> {

    List<Tip> findByMemberOrderByCreatedDateTimeDesc(Member member);


    @Query(value = "select * from tip where tip_id in " +
            "(select a.tip_id from tip_like a where a.member_id = :memberId )" +
            "order by created_date_time desc ", nativeQuery = true)
    List<Tip> findByMemberLike(@Param("memberId") long memberId);


    @Query(value = "select * from tip where tip_id in " +
            "(select a.tip_id from tip_bookmark a where a.member_id = :memberId ) " +
            "order by created_date_time desc", nativeQuery = true)
    List<Tip> findByMemberBookMark(@Param("memberId") long memberId);


    @Query(value = "select tip.* from (select b.tip_id, row_number() over(order by b.created_date_time desc) as row_num " +
            "from (select cf.* from (select p.*, TRIM(BOTH ' ' FROM REGEXP_REPLACE(p.content, '\\\\<.*?\\\\>', '')) AS clean_content\n" +
            "\t\t\t\t\t\tfrom tip p ) cf where cf.title like %:searchString% or cf.clean_content like %:searchString% ) as b ) as ranked_tip " +
            "inner join (select cf.* from (select p.*, TRIM(BOTH ' ' FROM REGEXP_REPLACE(p.content, '\\\\<.*?\\\\>', '')) AS clean_content\n" +
            "\t\t\t\t\t\tfrom tip p ) cf where cf.title like %:searchString% or cf.clean_content like %:searchString% ) as tip " +
            "on ranked_tip.tip_id = tip.tip_id " +
            "where ranked_tip.row_num > :page * :size " +
            "order by created_date_time desc limit :size ", nativeQuery = true)
    List<Tip> findAllSearch (@Param("searchString") String searchString, @Param("page") int page, @Param("size") int size);


    @Query(value = "select * from tip where tip_id in (select a.tip_id from tag a where a.tag_content = :searchTag ) order by created_date_time desc ", nativeQuery = true)
    List<Tip> findAllSearchTag (@Param("searchTag") String searchTag);


    @Query(value = "select count(*)\n" +
            "from ( select p.*, TRIM(BOTH ' ' FROM REGEXP_REPLACE(p.content, '\\\\<.*?\\\\>', '')) AS clean_content\n" +
            "from tip p) pp\n" +
            "where pp.title like %:searchString% or pp.clean_content like %:searchString% ", nativeQuery = true)
    Integer findAllCleanSearchCount (@Param("searchString") String searchString);


    @Query(value = "SELECT member_id FROM tip WHERE tip_id = :tipId", nativeQuery = true)
    Long findMemberIdByTipId(@Param("tipId") long tipId);
}
