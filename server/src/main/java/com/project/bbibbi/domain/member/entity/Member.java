package com.project.bbibbi.domain.member.entity;

import com.project.bbibbi.domain.showroom.feedComment.entity.FeedComment;
import com.project.bbibbi.domain.showroom.feedBookMark.entity.FeedBookMark;
import com.project.bbibbi.domain.showroom.feed.entity.Feed;
import com.project.bbibbi.domain.showroom.feedReply.entity.FeedReply;
import com.project.bbibbi.domain.showroom.feedReplyLike.entity.FeedReplyLike;
import com.project.bbibbi.domain.showroom.feedLike.entity.FeedLike;
import com.project.bbibbi.domain.follow.entity.Follow;
import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import com.project.bbibbi.domain.goodTip.tipBookmark.entity.TipBookmark;
import com.project.bbibbi.domain.goodTip.tipComment.entity.TipComment;
import com.project.bbibbi.domain.goodTip.tipLike.entity.TipLike;
import com.project.bbibbi.domain.goodTip.tipReply.entity.TipReply;
import com.project.bbibbi.domain.goodTip.tipReplyLike.entity.TipReplyLike;
import com.project.bbibbi.global.entity.BaseEntity;
import com.project.bbibbi.global.entity.Role;
import com.project.bbibbi.global.entity.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
//@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String myIntro;

    @Lob
    private String profileImg;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER

    private String socialId;

    private boolean checkUser;

    private String checkCode;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Tip> tips = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<TipReply> tipReplies = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<TipComment> tipComments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<TipBookmark> tipBookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<TipLike> tipLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<TipReplyLike> tipReplyLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<FeedReply> feedReplies = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<FeedComment> feedComments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<FeedBookMark> feedBookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<FeedLike> feedLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<FeedReplyLike> feedReplyLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Follow> follows = new ArrayList<>();

    @Builder
    private Member(Long memberId, String email, String nickname, String password,
                   String myIntro, String profileImg, Role role, SocialType socialType, String socialId, boolean checkUser) {
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.myIntro = myIntro;
        this.profileImg = profileImg;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
        this.checkUser = checkUser;

    }


    public static Member createMember(String email, String nickname, String password) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .role(Role.USER)
                .checkUser(false)
                .build();
    }

    public void updateMyInfo(String nickname, String myIntro, String profileImg) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (myIntro != null) {
            this.myIntro = myIntro;
        }
            this.profileImg = profileImg;
    }

    public void updatePassword(String newPassword) {

        this.password = newPassword;
    }

    public void authorizeUser() {
        this.role = Role.USER;
    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public void updateCheckCode(String updateCheckCode){
        this.checkCode = updateCheckCode;
    }

    public void updateCheckUser(boolean updateCheckUser) {
        this.checkUser = updateCheckUser;
    }


}
