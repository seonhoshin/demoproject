package com.project.bbibbi.domain.goodTip.tip.entity;

import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.goodTip.tipReply.entity.TipReply;
import com.project.bbibbi.domain.goodTip.tipTag.entity.Tag;
import com.project.bbibbi.global.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tip extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tipId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String coverPhoto;
    
    @Column(nullable = false)
    @Lob
    private String content;

    @Column
    private Integer views = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "tip", cascade = {CascadeType.ALL})
    private List<Tag> Tags = new ArrayList<>();

    @OneToMany(mappedBy = "tip", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private List<TipReply> replies;

    @Transient
    private int likeCount = 0;

    @Transient
    private Boolean likeYn = false;

    @Transient
    private int bookmarkCount = 0;

    @Transient
    private Boolean bookmarkYn = false;

    @Transient
    private Boolean finalPage = false;

    @Transient
    private Boolean followYn = false;

    @Transient
    private Boolean fixYn = false;

    public Long getAuthorId() {
        return member.getMemberId();
    }
}
