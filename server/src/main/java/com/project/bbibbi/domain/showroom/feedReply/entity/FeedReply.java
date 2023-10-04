package com.project.bbibbi.domain.showroom.feedReply.entity;

import com.project.bbibbi.domain.showroom.feed.entity.Feed;
import com.project.bbibbi.domain.showroom.feedComment.entity.FeedComment;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.global.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class FeedReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedReplyId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "feedReply", cascade = CascadeType.ALL)
    @OrderBy("id asc")
    private List<FeedComment> comments;
    @PrePersist
    protected void onCreate() {
        createdDateTime = LocalDateTime.now();
        modifiedDateTime = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        modifiedDateTime = LocalDateTime.now();
    }

    @Transient
    private Boolean replyLikeYn = false;
}

