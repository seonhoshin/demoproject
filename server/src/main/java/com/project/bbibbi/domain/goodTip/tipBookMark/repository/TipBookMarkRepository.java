package com.project.bbibbi.domain.goodTip.tipBookMark.repository;

import com.project.bbibbi.domain.goodTip.tipBookMark.entity.TipBookMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TipBookMarkRepository extends JpaRepository<TipBookMark, Long> {

    @Modifying
    @Query(value = "delete from tip_bookmark where tip_id = :tipId and member_id = :memberId ", nativeQuery = true)
    void deleteByTipIdAndMemberId(@Param("tipId") Long tipId, @Param("memberId") Long memberId);

    @Query(value = "select count(*) from tip_bookmark where tip_id = :tipId and member_id = :memberId ", nativeQuery = true)
    Integer existCount(@Param("tipId") Long tipId, @Param("memberId") Long memberId);

    @Query(value = "select count(*) from tip_bookmark where tip_id = :tipId", nativeQuery = true)
    Integer tipBookmarkCount(@Param("tipId") Long tipId);

    @Modifying
    @Query(value = "delete from tip_bookmark where tip_id = :tipId ", nativeQuery = true)
    void deleteByTipId(@Param("tipId") Long tipId);

}
