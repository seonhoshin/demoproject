package com.project.bbibbi.domain.goodTip.tipTag.entity;

import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import com.project.bbibbi.global.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column
    private String tagContent;

    @ManyToOne
    @JoinColumn(name = "tip_id", nullable = false)
    private Tip tip;

    @Builder
    public Tag(String tagContent) {
        this.tagContent = tagContent;
    }

}
