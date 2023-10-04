package com.project.bbibbi.domain.showroom.feed.entity;

//import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.showroom.feedReply.entity.FeedReply;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.global.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Feed extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 3000, nullable = false)
    @Lob
    private String content;

    @Column
    private Integer views = 0;

    @Column(length = 3000)
    @Lob
    private String coverPhoto;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private RoomType roomType;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private RoomSize roomSize;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private RoomCount roomCount;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private RoomInfo roomInfo;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "feed", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private List<FeedReply> replies;

    @Transient
    private int likeCount = 0;

    @Transient
    private Boolean likeYn = false;

    @Transient
    private int bookMarkCount = 0;

    @Transient
    private Boolean bookMarkYn = false;

    @Transient
    private Boolean finalPage = false;

    @Transient
    private Boolean followYn = false;
}
