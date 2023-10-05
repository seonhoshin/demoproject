package com.project.bbibbi.domain.showroom.feed.service;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.showroom.feed.entity.Feed;
//import com.project.bbibbi.domain.feed.entity.FeedImage;
//import com.project.bbibbi.domain.feed.entity.FeedImageTag;
//import com.project.bbibbi.domain.feed.repository.FeedImageRepository;
//import com.project.bbibbi.domain.feed.repository.FeedImageTagRepository;
import com.project.bbibbi.domain.showroom.feed.repository.FeedRepository;
import com.project.bbibbi.domain.showroom.feedBookMark.repository.FeedBookMarkRepository;
import com.project.bbibbi.domain.showroom.feedComment.repository.FeedCommentRepository;
import com.project.bbibbi.domain.showroom.feedReply.entity.FeedReply;
import com.project.bbibbi.domain.showroom.feedReplyLike.repository.FeedReplyLikeRepository;
import com.project.bbibbi.domain.showroom.feedLike.repository.FeedLikeRepository;
import com.project.bbibbi.domain.follow.repository.FollowRepository;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.member.repository.MemberRepository;
import com.project.bbibbi.global.entity.*;
import com.project.bbibbi.global.utils.CustomBeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class FeedService {
    private final FeedRepository feedRepository;

    private final FeedLikeRepository feedLikeRepository;
    private final FeedBookMarkRepository feedBookMarkRepository;
    private final FeedReplyLikeRepository feedReplyLikeRepository;
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final FeedCommentRepository feedCommentRepository;
    private final CustomBeanUtils<Feed> beanUtils;

    public FeedService(FeedRepository feedRepository,
                       FeedLikeRepository feedLikeRepository,
                       FeedBookMarkRepository feedBookMarkRepository,
                       FollowRepository followRepository,
                       FeedReplyLikeRepository feedReplyLikeRepository,
                       MemberRepository memberRepository,
                       FeedCommentRepository feedCommentRepository,
                       CustomBeanUtils<Feed> beanUtils) {
        this.feedRepository = feedRepository;
        this.feedLikeRepository = feedLikeRepository;
        this.feedBookMarkRepository = feedBookMarkRepository;
        this.followRepository = followRepository;
        this.feedReplyLikeRepository = feedReplyLikeRepository;
        this.memberRepository = memberRepository;
        this.feedCommentRepository = feedCommentRepository;
        this.beanUtils = beanUtils;
    }

    public Feed createFeed(Feed feed){

        Feed insertFeed = feedRepository.save(feed);

        Optional<Member> optionalMember = memberRepository.findById(feed.getMember().getMemberId());

        Member member = optionalMember.orElseThrow(() -> {throw new RuntimeException() ; });

        insertFeed.setMember(Member.builder().memberId(feed.getMember().getMemberId()).nickname(member.getNickname()).profileImg(member.getProfileImg()).build());

        return insertFeed;

    }


    public Feed updateFeed(Feed feed){

        Feed preFeed = findFeed(feed.getFeedId());

        long preFeedId = preFeed.getFeedId();
        ArrayList<Long> preFeedImgId = new ArrayList<>();

        Feed updatingFeed = feedRepository.save(feed);

        return updatingFeed;

    }


    public Feed findFeed(Long feedId){
        Feed findFeed = findVerifiedFeed(feedId);

        findFeed.setViews(findFeed.getViews() + 1);

        Feed viewUpFeed = feedRepository.save(findFeed);

        // 좋아요 개수
        Integer feedLikeCount = feedLikeRepository.feedLikeCount(viewUpFeed.getFeedId());
        viewUpFeed.setLikeCount(feedLikeCount);
        // 북마크 개수
        Integer bookmarkCount = feedBookMarkRepository.feedBookMarkCount(viewUpFeed.getFeedId());
        viewUpFeed.setBookMarkCount(bookmarkCount);

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        if(member == null){
            viewUpFeed.setLikeYn(false);
        }
        else {
            int loginUserLikeYn = feedLikeRepository.existCount(viewUpFeed.getFeedId(), member.getMemberId());
            if(loginUserLikeYn == 0)
                viewUpFeed.setLikeYn(false);
            else viewUpFeed.setLikeYn(true);
        }

        if(member == null){
            viewUpFeed.setBookMarkYn(false);
        }
        else {
            int loginUserLikeYn = feedBookMarkRepository.existCount(viewUpFeed.getFeedId(), member.getMemberId());
            if(loginUserLikeYn == 0) viewUpFeed.setBookMarkYn(false);
            else viewUpFeed.setBookMarkYn(true);
        }

        Integer existCount = followRepository.existCount(loginUtils.getLoginId(),viewUpFeed.getMember().getMemberId());

        if(existCount == 0){
            viewUpFeed.setFollowYn(false);
        }
        else {
            viewUpFeed.setFollowYn(true);
        }

        if(viewUpFeed.getReplies() != null) {
            if (member == null) {

                for (FeedReply feedReply : viewUpFeed.getReplies()) {

                    feedReply.setReplyLikeYn(false);
                }

            } else {

                for (FeedReply feedReply : viewUpFeed.getReplies()) {

                    int loginUserLikeYn = feedReplyLikeRepository.existCount(feedReply.getFeedReplyId(), member.getMemberId());

                    if (loginUserLikeYn == 0)
                        feedReply.setReplyLikeYn(false);
                    else feedReply.setReplyLikeYn(true);
                }
            }
        }

        return viewUpFeed;
    }


    public Page<Feed> findFeeds(String searchcode, int page, int size){

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdDateTime").descending());

        String whereCode = searchcode.substring(0, searchcode.length() - 2);

        Page<Feed> selectedFeeds = new PageImpl<>(new ArrayList<>());

        if(whereCode.equals("LOCATION")){

            selectedFeeds = feedRepository.findByLocation(Location.valueOf(searchcode), pageRequest);

            if(selectedFeeds.isLast()){
                for(Feed feed : selectedFeeds){
                    feed.setFinalPage(true);
                }
            }
        }
        else if(whereCode.equals("COUNT")){

            selectedFeeds = feedRepository.findByRoomCount(RoomCount.valueOf(searchcode), pageRequest);

            if(selectedFeeds.isLast()){
                for(Feed feed : selectedFeeds){
                    feed.setFinalPage(true);
                }
            }
        }
        else if(whereCode.equals("INFO")){

            selectedFeeds = feedRepository.findByRoomInfo(RoomInfo.valueOf(searchcode), pageRequest);

            if(selectedFeeds.isLast()){
                for(Feed feed : selectedFeeds){
                    feed.setFinalPage(true);
                }
            }
        }
        else if(whereCode.equals("SIZE")){

            selectedFeeds = feedRepository.findByRoomSize(RoomSize.valueOf(searchcode), pageRequest);

            if(selectedFeeds.isLast()){
                for(Feed feed : selectedFeeds){
                    feed.setFinalPage(true);
                }
            }
        }
        else if(whereCode.equals("TYPE")){

            selectedFeeds = feedRepository.findByRoomType(RoomType.valueOf(searchcode), pageRequest);

            if(selectedFeeds.isLast()){
                for(Feed feed : selectedFeeds){
                    feed.setFinalPage(true);
                }
            }
        }
        // 최신순 "RECENT00" -> "RECENT"
        else if(whereCode.equals("RECENT")){

            selectedFeeds = feedRepository.findByOrderByCreatedDateTimeDesc(pageRequest);
            if(selectedFeeds.isLast()){
                for(Feed feed : selectedFeeds){
                    feed.setFinalPage(true);
                }
            }
        }
        // 조회수순 "VIEW00" -> "VIEW"
        else if(whereCode.equals("VIEW")){

            selectedFeeds = feedRepository.findByOrderByViewsDesc(pageRequest);
            if(selectedFeeds.isLast()){
                for(Feed feed : selectedFeeds){
                    feed.setFinalPage(true);
                }
            }
        }

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        for(Feed feed : selectedFeeds){

            if(member == null){

                feed.setBookMarkYn(false);
            }
            else {
                int loginUserBookYn = feedBookMarkRepository.existCount(feed.getFeedId(), member.getMemberId());
                if(loginUserBookYn == 0) feed.setBookMarkYn(false);
                else feed.setBookMarkYn(true);
            }
        }

        for(Feed feed : selectedFeeds){
            Integer feedLikeCount = feedLikeRepository.feedLikeCount(feed.getFeedId());
            feed.setLikeCount(feedLikeCount);
        }

        for(Feed feed : selectedFeeds){
            Integer bookmarkCount = feedBookMarkRepository.feedBookMarkCount(feed.getFeedId());
            feed.setBookMarkCount(bookmarkCount);
        }

        return selectedFeeds;

    }

    public List<Feed> findSearchFeeds(String searchString, int page, int size){

        List<Feed> selectedFeeds = feedRepository.findBySearch(searchString, page, size);

        System.out.println("string : "+searchString);

        Integer selectedFeedsCount = feedRepository.findByCleanSearchCount(searchString);

        for(Feed feed : selectedFeeds){
            Integer feedLikeCount = feedLikeRepository.feedLikeCount(feed.getFeedId());
            feed.setLikeCount(feedLikeCount);
        }

        for(Feed feed : selectedFeeds){
            Integer bookmarkCount = feedBookMarkRepository.feedBookMarkCount(feed.getFeedId());
            feed.setBookMarkCount(bookmarkCount);
        }
        if(((page+1)*size) >= selectedFeedsCount){
            for(Feed feed : selectedFeeds){
                feed.setFinalPage(true);
            }
        }

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        for(Feed feed : selectedFeeds){

            if(member == null){

                feed.setBookMarkYn(false);
            }
            else {
                int loginUserBookYn = feedBookMarkRepository.existCount(feed.getFeedId(), member.getMemberId());
                if(loginUserBookYn == 0) feed.setBookMarkYn(false);
                else feed.setBookMarkYn(true);
            }
        }

        return selectedFeeds;

    }

    public List<Feed> findLikeTopTen(){

        List<Feed> selectedTopTenFeeds = feedRepository.findByLikeTopTen();

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        for(Feed feed : selectedTopTenFeeds){
            Integer feedLikeCount = feedLikeRepository.feedLikeCount(feed.getFeedId());
            feed.setLikeCount(feedLikeCount);
        }

        for(Feed feed : selectedTopTenFeeds){
            Integer bookmarkCount = feedBookMarkRepository.feedBookMarkCount(feed.getFeedId());
            feed.setBookMarkCount(bookmarkCount);
        }

        for(Feed feed : selectedTopTenFeeds){

            if(member == null){

                feed.setBookMarkYn(false);
            }
            else {
                int loginUserBookYn = feedBookMarkRepository.existCount(feed.getFeedId(), member.getMemberId());
                if(loginUserBookYn == 0) feed.setBookMarkYn(false);
                else feed.setBookMarkYn(true);
            }
        }

        return selectedTopTenFeeds;

    }


    public List<Feed> findMyInfoFeeds(long myInfoMemberId){

        List<Feed> pageFeeds = feedRepository.findByMemberOrderByCreatedDateTimeDesc(Member.builder().memberId(myInfoMemberId).build());

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        for(Feed feed : pageFeeds) {
            Integer feedLikeCount = feedLikeRepository.feedLikeCount(feed.getFeedId());
            feed.setLikeCount(feedLikeCount);
        }

        for(Feed feed : pageFeeds){
            Integer bookmarkCount = feedBookMarkRepository.feedBookMarkCount(feed.getFeedId());
            feed.setBookMarkCount(bookmarkCount);
        }

        for(Feed feed : pageFeeds){

            if(member == null){

                feed.setBookMarkYn(false);
            }
            else {
                int loginUserBookYn = feedBookMarkRepository.existCount(feed.getFeedId(), member.getMemberId());
                if(loginUserBookYn == 0) feed.setBookMarkYn(false);
                else feed.setBookMarkYn(true);
            }

        }
        return  pageFeeds;
    }

    public List<Feed> findMyInfoFeedsLike(long myInfoMemberId){

        List<Feed> pageFeeds = feedRepository.findByMemberLike(myInfoMemberId);

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        for(Feed feed : pageFeeds) {
            Integer feedLikeCount = feedLikeRepository.feedLikeCount(feed.getFeedId());
            feed.setLikeCount(feedLikeCount);
        }

        for(Feed feed : pageFeeds){
            Integer bookmarkCount = feedBookMarkRepository.feedBookMarkCount(feed.getFeedId());
            feed.setBookMarkCount(bookmarkCount);
        }

        for(Feed feed : pageFeeds){

            if(member == null){

                feed.setBookMarkYn(false);
            }
            else {
                int loginUserBookYn = feedBookMarkRepository.existCount(feed.getFeedId(), member.getMemberId());
                if(loginUserBookYn == 0) feed.setBookMarkYn(false);
                else feed.setBookMarkYn(true);
            }

        }
        return pageFeeds;
    }

    public List<Feed> findMyInfoFeedsBookMark(long myInfoMemberId){

        List<Feed> pageFeeds = feedRepository.findByMemberBookMark(myInfoMemberId);

        Member member = Member.builder().memberId(loginUtils.getLoginId()).build();

        for(Feed feed : pageFeeds) {
            Integer feedLikeCount = feedLikeRepository.feedLikeCount(feed.getFeedId());
            feed.setLikeCount(feedLikeCount);
        }

        for(Feed feed : pageFeeds){
            Integer bookmarkCount = feedBookMarkRepository.feedBookMarkCount(feed.getFeedId());
            feed.setBookMarkCount(bookmarkCount);
        }

        for(Feed feed : pageFeeds){

            if(member == null){

                feed.setBookMarkYn(false);
            }
            else {
                int loginUserBookYn = feedBookMarkRepository.existCount(feed.getFeedId(), member.getMemberId());
                if(loginUserBookYn == 0) feed.setBookMarkYn(false);
                else feed.setBookMarkYn(true);
            }
        }

        return pageFeeds;
    }


    public void deleteFeed(Long feedId){
        Feed feed = findVerifiedFeed(feedId);

        feedLikeRepository.deleteByFeedId(feedId);

        feedBookMarkRepository.deleteByFeedId(feedId);

        feedCommentRepository.deleteByFeedId(feedId);

        if(feed.getReplies() != null){
            for(FeedReply feedReply : feed.getReplies()){
                feedReplyLikeRepository.deleteByFeedReplyId(feedReply.getFeedReplyId());
            }
        }

        feedRepository.delete(feed);
    }

    public Feed findVerifiedFeed(Long feedId){
        Optional<Feed> optionalFeed = feedRepository.findById(feedId);

        Feed findFeed = optionalFeed.orElseThrow(() -> { throw new RuntimeException(); });

        return findFeed;
    }

}
