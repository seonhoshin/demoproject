package com.project.bbibbi.domain.goodTip.tip.service;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.goodTip.tip.repository.TipRepository;
import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import com.project.bbibbi.domain.goodTip.tipComment.repository.TipCommentRepository;
import com.project.bbibbi.domain.goodTip.tipReply.entity.TipReply;
import com.project.bbibbi.domain.follow.repository.FollowRepository;
import com.project.bbibbi.domain.member.repository.MemberRepository;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.goodTip.tipBookMark.repository.TipBookMarkRepository;
//import com.project.bbibbi.domain.tipImage.entity.TipImage;
//import com.project.bbibbi.domain.tipImage.service.TipImageService;
import com.project.bbibbi.domain.goodTip.tipLike.repository.TipLikeRepository;
//import com.project.bbibbi.domain.tipTag.entity.TipTag;
//import com.project.bbibbi.domain.tipTag.service.TipTagService;
import com.project.bbibbi.domain.goodTip.tipReplyLike.repository.TipReplyLikeRepository;
import com.project.bbibbi.domain.goodTip.tipTag.entity.Tag;
import com.project.bbibbi.domain.goodTip.tipTag.repository.TagRepository;
import com.project.bbibbi.global.exception.tipexception.TipNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TipService {
    private final TipRepository tipRepository;

    private final TipLikeRepository tipLikeRepository;

    private final TipBookMarkRepository tipBookmarkRepository;
    private final TipReplyLikeRepository tipReplyLikeRepository;
    private final FollowRepository followRepository;

    private final TipCommentRepository tipCommentRepository;

    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;

    public TipService(TipRepository tipRepository,
                      TipLikeRepository tipLikeRepository,
                      TipBookMarkRepository tipBookmarkRepository,
                      FollowRepository followRepository,
                      TipReplyLikeRepository tipReplyLikeRepository,
                      TipCommentRepository tipCommentRepository,
                      MemberRepository memberRepository,
                      TagRepository tagRepository) {
        this.tipRepository = tipRepository;
        this.tipLikeRepository = tipLikeRepository;
        this.tipBookmarkRepository = tipBookmarkRepository;
        this.tipReplyLikeRepository = tipReplyLikeRepository;
        this.followRepository = followRepository;
        this.memberRepository = memberRepository;
        this.tipCommentRepository = tipCommentRepository;
        this.tagRepository = tagRepository;

    }

    public Page<Tip> getAllTips(int page, int size) {

        PageRequest pageRequest =  PageRequest.of(page, size, Sort.by("createdDateTime").descending());

        Page<Tip> tipPages = tipRepository.findAll(pageRequest);

        if(tipPages.isLast()){
            for(Tip tip : tipPages){
                tip.setFinalPage(true);
            }
        }

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        for(Tip tip : tipPages){
            if(member == null){
                tip.setBookmarkYn(false);
            }
            else {
                int loginUserBookYn = tipBookmarkRepository.existCount(tip.getTipId(), member.getMemberId());
                if(loginUserBookYn == 0) tip.setBookmarkYn(false);
                else tip.setBookmarkYn(true);
            }
        }

        for(Tip tip : tipPages) {
            Integer tipLikeCount = tipLikeRepository.tipLikeCount(tip.getTipId());
            tip.setLikeCount(tipLikeCount);
        }

        for(Tip tip : tipPages){
            Integer bookmarkCount = tipBookmarkRepository.tipBookmarkCount(tip.getTipId());
            tip.setBookmarkCount(bookmarkCount);
        }

        return tipPages;
    }

    public List<Tip> findMyInfoTips(long myInfoMemberId){

        List<Tip> pageTips = tipRepository.findByMemberOrderByCreatedDateTimeDesc(Member.builder().memberId(myInfoMemberId).build()); // 비쿼리 성공

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        for(Tip tip : pageTips){
            if(member == null){
                tip.setBookmarkYn(false);
            }
            else {
                int loginUserBookYn = tipBookmarkRepository.existCount(tip.getTipId(), member.getMemberId());
                if(loginUserBookYn == 0) tip.setBookmarkYn(false);
                else tip.setBookmarkYn(true);
            }

        }

        for(Tip tip : pageTips) {
            Integer tipLikeCount = tipLikeRepository.tipLikeCount(tip.getTipId());
            tip.setLikeCount(tipLikeCount);
        }

        for(Tip tip : pageTips){
            Integer bookmarkCount = tipBookmarkRepository.tipBookmarkCount(tip.getTipId());
            tip.setBookmarkCount(bookmarkCount);
        }

        return  pageTips;
    }

    public List<Tip> findMyInfoTipsLike(long myInfoMemberId){

        List<Tip> pageTips = tipRepository.findByMemberLike(myInfoMemberId);

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        for(Tip tip : pageTips){
            if(member == null){
                tip.setBookmarkYn(false);
            }
            else {
                int loginUserBookYn = tipBookmarkRepository.existCount(tip.getTipId(), member.getMemberId());
                if(loginUserBookYn == 0) tip.setBookmarkYn(false);
                else tip.setBookmarkYn(true);
            }

        }

        return  pageTips;
    }

    public List<Tip> findMyInfoTipsBookMark(long myInfoMemberId){

        List<Tip> pageTips = tipRepository.findByMemberBookMark(myInfoMemberId);

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        for(Tip tip : pageTips){
            if(member == null){
                tip.setBookmarkYn(false);
            }
            else {
                int loginUserBookYn = tipBookmarkRepository.existCount(tip.getTipId(), member.getMemberId());
                if(loginUserBookYn == 0) tip.setBookmarkYn(false);
                else tip.setBookmarkYn(true);
            }

        }

        for(Tip tip : pageTips) {
            Integer tipLikeCount = tipLikeRepository.tipLikeCount(tip.getTipId());
            tip.setLikeCount(tipLikeCount);
        }

        for(Tip tip : pageTips){
            Integer bookmarkCount = tipBookmarkRepository.tipBookmarkCount(tip.getTipId());
            tip.setBookmarkCount(bookmarkCount);
        }

        return  pageTips;
    }

    public List<Tip> getAllSearchTips(String searchString, int page, int size) {

        List<Tip> searchTips = tipRepository.findAllSearch(searchString,page,size );

        Integer searchTipsCount = tipRepository.findAllCleanSearchCount(searchString);

        if(((page+1)*size) >= searchTipsCount){
            for(Tip tip : searchTips){
                tip.setFinalPage(true);
            }

        }

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        for(Tip tip : searchTips){
            if(member == null){
                tip.setBookmarkYn(false);
            }
            else {
                int loginUserBookYn = tipBookmarkRepository.existCount(tip.getTipId(), member.getMemberId());
                if(loginUserBookYn == 0) tip.setBookmarkYn(false);
                else tip.setBookmarkYn(true);
            }

        }

        for(Tip tip : searchTips) {
            Integer tipLikeCount = tipLikeRepository.tipLikeCount(tip.getTipId());
            tip.setLikeCount(tipLikeCount);
        }

        for(Tip tip : searchTips){
            Integer bookmarkCount = tipBookmarkRepository.tipBookmarkCount(tip.getTipId());
            tip.setBookmarkCount(bookmarkCount);
        }

        return searchTips;
    }

    public List<Tip> getAllSearchTipTags(String searchTag) {

        List<Tip> searchTips = tipRepository.findAllSearchTag(searchTag);

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        for(Tip tip : searchTips){
            if(member == null){
                tip.setBookmarkYn(false);
            }
            else {
                int loginUserBookYn = tipBookmarkRepository.existCount(tip.getTipId(), member.getMemberId());
                if(loginUserBookYn == 0) tip.setBookmarkYn(false);
                else tip.setBookmarkYn(true);
            }

        }

        for(Tip tip : searchTips) {
            Integer tipLikeCount = tipLikeRepository.tipLikeCount(tip.getTipId());
            tip.setLikeCount(tipLikeCount);
        }

        for(Tip tip : searchTips){
            Integer bookmarkCount = tipBookmarkRepository.tipBookmarkCount(tip.getTipId());
            tip.setBookmarkCount(bookmarkCount);
        }

        return searchTips;
    }

    public Tip getTip(Long tipId) {
        Tip findTip = findVerifiedTip(tipId);

        findTip.setViews(findTip.getViews() + 1);

        Tip viewUpTip = tipRepository.save(findTip);

        Integer tipLikeCount = tipLikeRepository.tipLikeCount(viewUpTip.getTipId());
        viewUpTip.setLikeCount(tipLikeCount);

        Integer bookmarkCount = tipBookmarkRepository.tipBookmarkCount(viewUpTip.getTipId());
        viewUpTip.setBookmarkCount(bookmarkCount);

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        if(member == null){
            viewUpTip.setLikeYn(false);
        }
        else {
            int loginUserLikeYn = tipLikeRepository.existCount(viewUpTip.getTipId(), member.getMemberId());
            if(loginUserLikeYn == 0)
                viewUpTip.setLikeYn(false);
            else viewUpTip.setLikeYn(true);
        }

        if(member == null){
            viewUpTip.setBookmarkYn(false);
        }
        else {
            int loginUserLikeYn = tipBookmarkRepository.existCount(viewUpTip.getTipId(), member.getMemberId());
            if(loginUserLikeYn == 0) viewUpTip.setBookmarkYn(false);
            else viewUpTip.setBookmarkYn(true);
        }

        Integer existCount = followRepository.existCount(loginUtils.getLoginId(),viewUpTip.getMember().getMemberId());

        if(existCount == 0){
            viewUpTip.setFollowYn(false);
        }
        else {
            viewUpTip.setFollowYn(true);
        }

        if(loginUtils.getLoginId() == null){
            viewUpTip.setFixYn(false);
        }
        else {
            long tipAuthorId = viewUpTip.getMember().getMemberId();
            if(tipAuthorId != loginUtils.getLoginId())
                viewUpTip.setFixYn(false);
            else viewUpTip.setFixYn(true);
        }

        if(viewUpTip.getReplies() != null) {
            if (member == null) {

                for (TipReply tipReply : viewUpTip.getReplies()) {

                    tipReply.setReplyLikeYn(false);
                }

            } else {

                for (TipReply tipReply : viewUpTip.getReplies()) {

                    int loginUserLikeYn = tipReplyLikeRepository.existCount(tipReply.getTipReplyId(), member.getMemberId());

                    if (loginUserLikeYn == 0)
                        tipReply.setReplyLikeYn(false);
                    else tipReply.setReplyLikeYn(true);
                }
            }
        }

        return viewUpTip;
    }


    public Tip findVerifiedTip(Long tipId){
        Optional<Tip> optionalTip = tipRepository.findById(tipId);

        Tip findTip = optionalTip.orElseThrow(() -> { throw new TipNotFoundException(); });

        return findTip;
    }

    public Tip createTip(Tip tip) {

        Tip insertTip = tipRepository.save(tip);

        Optional<Member> optionalMember = memberRepository.findById(tip.getMember().getMemberId());

        Member member = optionalMember.orElseThrow(() -> {throw new RuntimeException() ; });

        insertTip.setMember(Member.builder().memberId(tip.getMember().getMemberId()).nickname(member.getNickname()).profileImg(member.getProfileImg()).build());

        return insertTip;
    }

    public Tip updateTip(Long tipId, Tip updatedTip) {
        Tip tip = tipRepository.findById(tipId)
                .orElseThrow();

        updatedTip.setCreatedDateTime(tip.getCreatedDateTime());

        tip.setTitle(updatedTip.getTitle());
        tip.setCoverPhoto(updatedTip.getCoverPhoto());
        tip.setContent(updatedTip.getContent());

        tagRepository.deleteByTipId(tipId);

        Tip updatingTip = tipRepository.save(tip);

        updateTags(updatedTip, tipId);

        return updatingTip;
    }

    public void updateTags(Tip tip, long tipId){

        tip.setTipId(tipId);

        if(tip.getTags() != null) {
            for (Tag tag : tip.getTags()) {
                tagRepository.save(tag);
            }
        }
    }

    public void deleteTip(Long tipId) {
        Tip tip = findVerifiedTip(tipId);

        tipLikeRepository.deleteByTipId(tipId);

        tipBookmarkRepository.deleteByTipId(tipId);

        tipCommentRepository.deleteByTipId(tipId);

        if(tip.getReplies() != null){
            for(TipReply tipReply : tip.getReplies()){
                tipReplyLikeRepository.deleteByTipReplyId(tipReply.getTipReplyId());
            }
        }

        tipRepository.delete(tip);
    }

}
