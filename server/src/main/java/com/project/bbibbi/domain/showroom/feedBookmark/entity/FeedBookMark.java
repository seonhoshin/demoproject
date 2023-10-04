package com.project.bbibbi.domain.showroom.feedBookmark.entity;

import com.project.bbibbi.domain.showroom.feed.entity.Feed;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FeedBookMark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedBookMarkId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Transient
    private Boolean BookMarkYn = false;

    @Transient
    private Integer BookMarkCount = 0;

}
