package com.project.bbibbi.domain.showroom.feed;

import com.google.gson.Gson;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.showroom.feed.dto.FeedPatchDto;
import com.project.bbibbi.domain.showroom.feed.dto.FeedPostDto;
import com.project.bbibbi.domain.showroom.feed.dto.FeedResponseDto;
import com.project.bbibbi.domain.showroom.feed.entity.Feed;
import com.project.bbibbi.domain.showroom.feed.mapper.FeedMapper;
import com.project.bbibbi.domain.showroom.feed.service.FeedService;
import com.project.bbibbi.domain.showroom.feedComment.dto.FeedCommentDto;
import com.project.bbibbi.domain.showroom.feedComment.entity.FeedComment;
import com.project.bbibbi.domain.showroom.feedReply.dto.FeedReplyResponseDto;
import com.project.bbibbi.domain.showroom.feedReply.entity.FeedReply;
import com.project.bbibbi.global.entity.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class FeedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private FeedService feedService;

    @MockBean
    private FeedMapper mapper;

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void postFeedTest() throws Exception {

        FeedPostDto post = new FeedPostDto();
        post.setTitle("title1");
        post.setContent("content1");
        post.setCoverPhoto("coverphoto1");
        post.setRoomType("TYPE02");
        post.setRoomSize("SIZE03");
        post.setRoomCount("COUNT05");
        post.setRoomInfo("INFO01");
        post.setLocation("LOCATION03");

        String content = gson.toJson(post);

        given(mapper.feedPostDtoToFeed(Mockito.any(FeedPostDto.class))).willReturn(new Feed());

        given(feedService.createFeed(Mockito.any(Feed.class))).willReturn(new Feed());

        FeedResponseDto mockFeedResDto = new FeedResponseDto();

        mockFeedResDto.setFeedId(1L);
        mockFeedResDto.setTitle(post.getTitle());
        mockFeedResDto.setContent(post.getContent());
        mockFeedResDto.setCoverPhoto(post.getCoverPhoto());
        mockFeedResDto.setRoomType(post.getRoomType());
        mockFeedResDto.setRoomSize(post.getRoomSize());
        mockFeedResDto.setRoomCount(post.getRoomCount());
        mockFeedResDto.setRoomInfo(post.getRoomInfo());
        mockFeedResDto.setLocation(post.getLocation());

        given(mapper.feedToFeedResponseDto(Mockito.any(Feed.class))).willReturn(mockFeedResDto);

        ResultActions actions =
                mockMvc.perform(
                        post("/feed")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.feedId").value(mockFeedResDto.getFeedId()))
                .andExpect(jsonPath("$.data.title").value(post.getTitle()))
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andExpect(jsonPath("$.data.roomType").value(post.getRoomType()));

    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void patchFeedTest() throws Exception {

        long feedId = 1L;

        FeedPatchDto patch = new FeedPatchDto();
        patch.setFeedId(feedId);
        patch.setTitle("title1");
        patch.setContent("content1");
        patch.setCoverPhoto("coverphoto1");
        patch.setRoomType("TYPE02");
        patch.setRoomSize("SIZE03");
        patch.setRoomCount("COUNT05");
        patch.setRoomInfo("INFO01");
        patch.setLocation("LOCATION03");

        String content = gson.toJson(patch);

        given(mapper.feedPatchDtoToFeed(Mockito.any(FeedPatchDto.class))).willReturn(new Feed());

        given(feedService.updateFeed(Mockito.any(Feed.class))).willReturn(new Feed());

        FeedResponseDto feedResponseDto = new FeedResponseDto();

        feedResponseDto.setFeedId(1L);
        feedResponseDto.setTitle(patch.getTitle());
        feedResponseDto.setContent(patch.getContent());
        feedResponseDto.setCoverPhoto(patch.getCoverPhoto());
        feedResponseDto.setRoomType(patch.getRoomType());
        feedResponseDto.setRoomSize(patch.getRoomSize());
        feedResponseDto.setRoomCount(patch.getRoomCount());
        feedResponseDto.setRoomInfo(patch.getRoomInfo());
        feedResponseDto.setLocation(patch.getLocation());

        given(mapper.feedToFeedResponseDto(Mockito.any(Feed.class))).willReturn(feedResponseDto);

        ResultActions actions =
                mockMvc.perform(
                        patch("/feed/{feed-id}", feedId )
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.feedId").value(feedId))
                .andExpect(jsonPath("$.data.title").value(patch.getTitle()))
                .andExpect(jsonPath("$.data.content").value(patch.getContent()))
                .andExpect(jsonPath("$.data.coverPhoto").value(patch.getCoverPhoto()))
                .andExpect(jsonPath("$.data.roomSize").value(patch.getRoomSize()));

    }

    @Test
    void getFeedTest() throws Exception {
        long feedId = 1L;

        Feed feed = new Feed();

        feed.setFeedId(feedId);
        feed.setCreatedDateTime(LocalDateTime.now());
        feed.setModifiedDateTime(LocalDateTime.now());
        feed.setTitle("title1");
        feed.setContent("content1");
        feed.setViews(0);
        feed.setCoverPhoto("coverPhoto1");
        feed.setRoomType(RoomType.TYPE01);
        feed.setRoomSize(RoomSize.SIZE01);
        feed.setRoomCount(RoomCount.COUNT01);
        feed.setRoomInfo(RoomInfo.INFO01);
        feed.setLocation(Location.LOCATION01);

        feed.setMember(Member.builder().memberId(1L).nickname("nickname1").build());

        List<FeedReply> feedReplyList = new ArrayList<>();
        FeedReply feedReply = new FeedReply();
        feedReply.setFeedReplyId(1L);
        feedReply.setContent("Rcontent1");
        feedReply.setFeed(feed);
        feedReply.setMember(feed.getMember());
        feedReply.setCreatedDateTime(LocalDateTime.now());
        feedReply.setModifiedDateTime(LocalDateTime.now());

        List<FeedComment> feedCommentList = new ArrayList<>();
        FeedComment feedComment = new FeedComment();
        feedComment.setFeedCommentId(1L);
        feedComment.setFeed(feed);
        feedComment.setFeedReply(feedReply);
        feedComment.setMember(feed.getMember());
        feedComment.setCreatedDateTime(LocalDateTime.now());
        feedComment.setContent("Rcontent1Comment1");
        feedComment.setParentComment(null);

        feedCommentList.add(feedComment);

        feedReply.setComments(feedCommentList);

        feedReplyList.add(feedReply);

        feed.setReplies(feedReplyList);

        given(feedService.findFeed(Mockito.anyLong())).willReturn(new Feed());

        FeedResponseDto feedResponseDto = new FeedResponseDto();
        feedResponseDto.setFeedId(feed.getFeedId());
        feedResponseDto.setCreatedDateTime(feed.getCreatedDateTime());
        feedResponseDto.setModifiedDateTime(feed.getModifiedDateTime());
        feedResponseDto.setTitle(feed.getTitle());
        feedResponseDto.setContent(feed.getContent());
        feedResponseDto.setCoverPhoto(feed.getCoverPhoto());
        feedResponseDto.setMemberId(feed.getMember().getMemberId());
        feedResponseDto.setNickname(feed.getMember().getNickname());
        feedResponseDto.setRoomType(feed.getRoomType().toString());
        feedResponseDto.setRoomTypeName(feed.getRoomType().getDescription());
        feedResponseDto.setRoomSize(feed.getRoomSize().toString());
        feedResponseDto.setRoomSizeName(feed.getRoomSize().getDescription());
        feedResponseDto.setRoomCount(feed.getRoomCount().toString());
        feedResponseDto.setRoomCountName(feed.getRoomCount().getDescription());
        feedResponseDto.setRoomInfo(feed.getRoomInfo().toString());
        feedResponseDto.setRoomInfoName(feed.getRoomInfo().getDescription());
        feedResponseDto.setLocation(feed.getLocation().toString());
        feedResponseDto.setLocationName(feed.getLocation().getDescription());

        List<FeedReplyResponseDto> feedReplyResponseDtos = new ArrayList<>();

        FeedReplyResponseDto feedReplyResponseDto = new FeedReplyResponseDto();

        feedReplyResponseDto.setFeedReplyId(feedReply.getFeedReplyId());
        feedReplyResponseDto.setContent(feedReply.getContent());
        feedReplyResponseDto.setNickname(feedReply.getMember().getNickname());
        feedReplyResponseDto.setMemberId(feedReply.getMember().getMemberId());
        feedReplyResponseDto.setFeedId(feedReply.getFeed().getFeedId());
        feedReplyResponseDto.setCreatedDateTime(feedReply.getCreatedDateTime());
        feedReplyResponseDto.setModifiedDateTime(feedReply.getModifiedDateTime());

        List<FeedCommentDto> feedCommentDtoList = new ArrayList<>();
        FeedCommentDto feedCommentDto = new FeedCommentDto();

        feedCommentDto.setFeedCommentId(feedReply.getComments().get(0).getFeedCommentId());
        feedCommentDto.setFeedId(feedReply.getComments().get(0).getFeed().getFeedId());
        feedCommentDto.setFeedReplyId(feedReply.getComments().get(0).getFeedReply().getFeedReplyId());
        feedCommentDto.setMemberId(feedReply.getComments().get(0).getMember().getMemberId());
        feedCommentDto.setContent(feedReply.getComments().get(0).getContent());
        feedCommentDto.setCreatedDateTime(feedReply.getComments().get(0).getCreatedDateTime());

        feedCommentDtoList.add(feedCommentDto);
        feedReplyResponseDto.setComments(feedCommentDtoList);

        feedReplyResponseDtos.add(feedReplyResponseDto);

        feedResponseDto.setReplies(feedReplyResponseDtos);

        given(mapper.feedToFeedResponseDto(Mockito.any(Feed.class))).willReturn(feedResponseDto);

        URI uri = UriComponentsBuilder.newInstance().path("/feed/{feed-id}").buildAndExpand(feedId).toUri();

        ResultActions actions =
                mockMvc.perform(
                                get("/feed/{feed-id}", feedId)
                                .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.feedId").value(feed.getFeedId()))
                .andExpect(jsonPath("$.data.title").value(feed.getTitle()))
                .andExpect(jsonPath("$.data.content").value(feed.getContent()))
                .andExpect(jsonPath("$.data.roomType").value(feed.getRoomType().toString()))
                .andExpect(jsonPath("$.data.roomTypeName").value(feed.getRoomType().getDescription()))
                .andExpect(jsonPath("$.data.memberId").value(feed.getMember().getMemberId()))
                .andExpect(jsonPath("$.data.nickname").value(feed.getMember().getNickname()))
                .andExpect(jsonPath("$.data.replies[0].feedReplyId").value(feed.getReplies().get(0).getFeedReplyId()))
                .andExpect(jsonPath("$.data.replies[0].content").value(feed.getReplies().get(0).getContent()))
                .andExpect(jsonPath("$.data.replies[0].feedId").value(feed.getReplies().get(0).getFeed().getFeedId()))
                .andExpect(jsonPath("$.data.replies[0].nickname").value(feed.getReplies().get(0).getMember().getNickname()))
                .andExpect(jsonPath("$.data.replies[0].comments[0].feedCommentId").value((feed.getReplies().get(0).getComments().get(0).getFeedCommentId())))
                .andExpect(jsonPath("$.data.replies[0].comments[0].feedReplyId").value(feed.getReplies().get(0).getComments().get(0).getFeedReply().getFeedReplyId()))
                .andExpect(jsonPath("$.data.replies[0].comments[0].content").value(feed.getReplies().get(0).getComments().get(0).getContent()));

    }

    @Test
    void getFeedsTest() throws Exception {

        Feed feed1 = new Feed();

        feed1.setFeedId(1L);
        feed1.setCreatedDateTime(LocalDateTime.now());
        feed1.setModifiedDateTime(LocalDateTime.now());
        feed1.setTitle("title1");
        feed1.setContent("content1");
        feed1.setViews(0);
        feed1.setCoverPhoto("coverPhoto1");
        feed1.setRoomType(RoomType.TYPE01);
        feed1.setRoomSize(RoomSize.SIZE01);
        feed1.setRoomCount(RoomCount.COUNT01);
        feed1.setRoomInfo(RoomInfo.INFO01);
        feed1.setLocation(Location.LOCATION01);

        feed1.setMember(Member.builder().memberId(1L).nickname("nickname1").build());

        List<FeedReply> feedReplyList1 = new ArrayList<>();
        FeedReply feedReply1 = new FeedReply();
        feedReply1.setFeedReplyId(1L);
        feedReply1.setContent("Rcontent1");
        feedReply1.setFeed(feed1);
        feedReply1.setMember(feed1.getMember());
        feedReply1.setCreatedDateTime(LocalDateTime.now());
        feedReply1.setModifiedDateTime(LocalDateTime.now());
        feedReplyList1.add(feedReply1);

        feed1.setReplies(feedReplyList1);


        Feed feed2 = new Feed();

        feed2.setFeedId(2L);
        feed2.setCreatedDateTime(LocalDateTime.now());
        feed2.setModifiedDateTime(LocalDateTime.now());
        feed2.setTitle("title11");
        feed2.setContent("content11");
        feed2.setViews(0);
        feed2.setCoverPhoto("coverPhoto11");
        feed2.setRoomType(RoomType.TYPE02);
        feed2.setRoomSize(RoomSize.SIZE02);
        feed2.setRoomCount(RoomCount.COUNT02);
        feed2.setRoomInfo(RoomInfo.INFO02);
        feed2.setLocation(Location.LOCATION02);

        feed2.setMember(Member.builder().memberId(2L).nickname("nickname11").build());

        List<FeedReply> feedReplyList2 = new ArrayList<>();
        FeedReply feedReply2 = new FeedReply();
        feedReply2.setFeedReplyId(2L);
        feedReply2.setContent("Rcontent11");
        feedReply2.setFeed(feed2);
        feedReply2.setMember(feed2.getMember());
        feedReply2.setCreatedDateTime(LocalDateTime.now());
        feedReply2.setModifiedDateTime(LocalDateTime.now());
        feedReplyList2.add(feedReply2);

        feed2.setReplies(feedReplyList2);

        List<Feed> feedList = new ArrayList<>();
        feedList.add(feed1);
        feedList.add(feed2);

        Page<Feed> pageFeeds = new PageImpl<>(feedList);

        given(feedService.findFeeds(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).willReturn(pageFeeds);

        List<Feed> afterFeeds = pageFeeds.getContent();


        FeedResponseDto feedResponseDto1 = new FeedResponseDto();
        feedResponseDto1.setFeedId(afterFeeds.get(0).getFeedId());
        feedResponseDto1.setCreatedDateTime(afterFeeds.get(0).getCreatedDateTime());
        feedResponseDto1.setModifiedDateTime(afterFeeds.get(0).getModifiedDateTime());
        feedResponseDto1.setTitle(afterFeeds.get(0).getTitle());
        feedResponseDto1.setContent(afterFeeds.get(0).getContent());
        feedResponseDto1.setCoverPhoto(afterFeeds.get(0).getCoverPhoto());
        feedResponseDto1.setMemberId(afterFeeds.get(0).getMember().getMemberId());
        feedResponseDto1.setNickname(afterFeeds.get(0).getMember().getNickname());
        feedResponseDto1.setRoomType(afterFeeds.get(0).getRoomType().toString());
        feedResponseDto1.setRoomTypeName(afterFeeds.get(0).getRoomType().getDescription());
        feedResponseDto1.setRoomSize(afterFeeds.get(0).getRoomSize().toString());
        feedResponseDto1.setRoomSizeName(afterFeeds.get(0).getRoomSize().getDescription());
        feedResponseDto1.setRoomCount(afterFeeds.get(0).getRoomCount().toString());
        feedResponseDto1.setRoomCountName(afterFeeds.get(0).getRoomCount().getDescription());
        feedResponseDto1.setRoomInfo(afterFeeds.get(0).getRoomInfo().toString());
        feedResponseDto1.setRoomInfoName(afterFeeds.get(0).getRoomInfo().getDescription());
        feedResponseDto1.setLocation(afterFeeds.get(0).getLocation().toString());
        feedResponseDto1.setLocationName(afterFeeds.get(0).getLocation().getDescription());

        List<FeedReplyResponseDto> feedReplyResponseDtos1 = new ArrayList<>();

        FeedReplyResponseDto feedReplyResponseDto1 = new FeedReplyResponseDto();

        feedReplyResponseDto1.setFeedReplyId(afterFeeds.get(0).getReplies().get(0).getFeedReplyId());
        feedReplyResponseDto1.setContent(afterFeeds.get(0).getReplies().get(0).getContent());
        feedReplyResponseDto1.setNickname(afterFeeds.get(0).getReplies().get(0).getMember().getNickname());
        feedReplyResponseDto1.setMemberId(afterFeeds.get(0).getReplies().get(0).getMember().getMemberId());
        feedReplyResponseDto1.setFeedId(afterFeeds.get(0).getReplies().get(0).getFeed().getFeedId());
        feedReplyResponseDto1.setCreatedDateTime(afterFeeds.get(0).getReplies().get(0).getCreatedDateTime());
        feedReplyResponseDto1.setModifiedDateTime(afterFeeds.get(0).getReplies().get(0).getModifiedDateTime());

        feedReplyResponseDtos1.add(feedReplyResponseDto1);

        feedResponseDto1.setReplies(feedReplyResponseDtos1);


        FeedResponseDto feedResponseDto2 = new FeedResponseDto();
        feedResponseDto2.setFeedId(afterFeeds.get(1).getFeedId());
        feedResponseDto2.setCreatedDateTime(afterFeeds.get(1).getCreatedDateTime());
        feedResponseDto2.setModifiedDateTime(afterFeeds.get(1).getModifiedDateTime());
        feedResponseDto2.setTitle(afterFeeds.get(1).getTitle());
        feedResponseDto2.setContent(afterFeeds.get(1).getContent());
        feedResponseDto2.setCoverPhoto(afterFeeds.get(1).getCoverPhoto());
        feedResponseDto2.setMemberId(afterFeeds.get(1).getMember().getMemberId());
        feedResponseDto2.setNickname(afterFeeds.get(1).getMember().getNickname());
        feedResponseDto2.setRoomType(afterFeeds.get(1).getRoomType().toString());
        feedResponseDto2.setRoomTypeName(afterFeeds.get(1).getRoomType().getDescription());
        feedResponseDto2.setRoomSize(afterFeeds.get(1).getRoomSize().toString());
        feedResponseDto2.setRoomSizeName(afterFeeds.get(1).getRoomSize().getDescription());
        feedResponseDto2.setRoomCount(afterFeeds.get(1).getRoomCount().toString());
        feedResponseDto2.setRoomCountName(afterFeeds.get(1).getRoomCount().getDescription());
        feedResponseDto2.setRoomInfo(afterFeeds.get(1).getRoomInfo().toString());
        feedResponseDto2.setRoomInfoName(afterFeeds.get(1).getRoomInfo().getDescription());
        feedResponseDto2.setLocation(afterFeeds.get(1).getLocation().toString());
        feedResponseDto2.setLocationName(afterFeeds.get(1).getLocation().getDescription());

        List<FeedReplyResponseDto> feedReplyResponseDtos2 = new ArrayList<>();

        FeedReplyResponseDto feedReplyResponseDto2 = new FeedReplyResponseDto();

        feedReplyResponseDto2.setFeedReplyId(afterFeeds.get(1).getReplies().get(0).getFeedReplyId());
        feedReplyResponseDto2.setContent(afterFeeds.get(1).getReplies().get(0).getContent());
        feedReplyResponseDto2.setNickname(afterFeeds.get(1).getReplies().get(0).getMember().getNickname());
        feedReplyResponseDto2.setMemberId(afterFeeds.get(1).getReplies().get(0).getMember().getMemberId());
        feedReplyResponseDto2.setFeedId(afterFeeds.get(1).getReplies().get(0).getFeed().getFeedId());
        feedReplyResponseDto2.setCreatedDateTime(afterFeeds.get(1).getReplies().get(0).getCreatedDateTime());
        feedReplyResponseDto2.setModifiedDateTime(afterFeeds.get(1).getReplies().get(0).getModifiedDateTime());

        feedReplyResponseDtos2.add(feedReplyResponseDto2);

        feedResponseDto2.setReplies(feedReplyResponseDtos2);

        List<FeedResponseDto> feedResponseDtos = new ArrayList<>();

        feedResponseDtos.add(feedResponseDto1);
        feedResponseDtos.add(feedResponseDto2);

        given(mapper.feedsToFeedResponseDtos(Mockito.anyList())).willReturn(feedResponseDtos);

        String searchCode = "RECENT00";
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap();
        queryParams.add("page", "1");

        ResultActions actions =
                mockMvc.perform(
                                get("/feed/filter/{search-code}", searchCode)
                                        .params(queryParams)
                                .accept(MediaType.APPLICATION_JSON)
                );

        ResultActions result = actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].feedId").value(feed1.getFeedId()))
                .andExpect(jsonPath("$.data[0].title").value(feed1.getTitle()))
                .andExpect(jsonPath("$.data[0].memberId").value(feed1.getMember().getMemberId()))
                .andExpect(jsonPath("$.data[0].roomType").value(feed1.getRoomType().toString()))
                .andExpect(jsonPath("$.data[0].replies[0].content").value(feed1.getReplies().get(0).getContent()))
                .andExpect(jsonPath("$.data[0].replies[0].nickname").value(feed1.getReplies().get(0).getMember().getNickname()))
                .andExpect(jsonPath("$.data[1].title").value(feed2.getTitle()))
                .andExpect(jsonPath("$.data[1].content").value(feed2.getContent()))
                .andExpect(jsonPath("$.data[1].nickname").value(feed2.getMember().getNickname()))
                .andExpect(jsonPath("$.data[1].roomCount").value(feed2.getRoomCount().toString()))
                .andExpect(jsonPath("$.data[1].roomCountName").value(feed2.getRoomCount().getDescription()))
                .andExpect(jsonPath("$.data[1].replies[0].feedId").value(feed2.getReplies().get(0).getFeed().getFeedId()))
                .andExpect(jsonPath("$.data[1].replies[0].content").value(feed2.getReplies().get(0).getContent()));

    }

    @Test
    void getSearchFeedsTest() throws Exception {
        Feed feed1 = new Feed();

        feed1.setFeedId(1L);
        feed1.setCreatedDateTime(LocalDateTime.now());
        feed1.setModifiedDateTime(LocalDateTime.now());
        feed1.setTitle("title11");
        feed1.setContent("content11");
        feed1.setViews(0);
        feed1.setCoverPhoto("coverPhoto11");
        feed1.setRoomType(RoomType.TYPE03);
        feed1.setRoomSize(RoomSize.SIZE03);
        feed1.setRoomCount(RoomCount.COUNT03);
        feed1.setRoomInfo(RoomInfo.INFO03);
        feed1.setLocation(Location.LOCATION03);

        feed1.setMember(Member.builder().memberId(1L).nickname("nickname11").build());

        List<FeedReply> feedReplyList1 = new ArrayList<>();
        FeedReply feedReply1 = new FeedReply();
        feedReply1.setFeedReplyId(1L);
        feedReply1.setContent("Rcontent11");
        feedReply1.setFeed(feed1);
        feedReply1.setMember(feed1.getMember());
        feedReply1.setCreatedDateTime(LocalDateTime.now());
        feedReply1.setModifiedDateTime(LocalDateTime.now());
        feedReplyList1.add(feedReply1);

        feed1.setReplies(feedReplyList1);


        Feed feed2 = new Feed();

        feed2.setFeedId(2L);
        feed2.setCreatedDateTime(LocalDateTime.now());
        feed2.setModifiedDateTime(LocalDateTime.now());
        feed2.setTitle("title111");
        feed2.setContent("content111");
        feed2.setViews(0);
        feed2.setCoverPhoto("coverPhoto111");
        feed2.setRoomType(RoomType.TYPE04);
        feed2.setRoomSize(RoomSize.SIZE04);
        feed2.setRoomCount(RoomCount.COUNT04);
        feed2.setRoomInfo(RoomInfo.INFO04);
        feed2.setLocation(Location.LOCATION04);

        feed2.setMember(Member.builder().memberId(2L).nickname("nickname111").build());

        List<FeedReply> feedReplyList2 = new ArrayList<>();
        FeedReply feedReply2 = new FeedReply();
        feedReply2.setFeedReplyId(2L);
        feedReply2.setContent("Rcontent111");
        feedReply2.setFeed(feed2);
        feedReply2.setMember(feed2.getMember());
        feedReply2.setCreatedDateTime(LocalDateTime.now());
        feedReply2.setModifiedDateTime(LocalDateTime.now());
        feedReplyList2.add(feedReply2);

        feed2.setReplies(feedReplyList2);

        List<Feed> feedList = new ArrayList<>();
        feedList.add(feed1);
        feedList.add(feed2);

        Page<Feed> pageFeeds = new PageImpl<>(feedList);

        given(feedService.findFeeds(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).willReturn(pageFeeds);

        List<Feed> afterFeeds = pageFeeds.getContent();


        FeedResponseDto feedResponseDto1 = new FeedResponseDto();
        feedResponseDto1.setFeedId(afterFeeds.get(0).getFeedId());
        feedResponseDto1.setCreatedDateTime(afterFeeds.get(0).getCreatedDateTime());
        feedResponseDto1.setModifiedDateTime(afterFeeds.get(0).getModifiedDateTime());
        feedResponseDto1.setTitle(afterFeeds.get(0).getTitle());
        feedResponseDto1.setContent(afterFeeds.get(0).getContent());
        feedResponseDto1.setCoverPhoto(afterFeeds.get(0).getCoverPhoto());
        feedResponseDto1.setMemberId(afterFeeds.get(0).getMember().getMemberId());
        feedResponseDto1.setNickname(afterFeeds.get(0).getMember().getNickname());
        feedResponseDto1.setRoomType(afterFeeds.get(0).getRoomType().toString());
        feedResponseDto1.setRoomTypeName(afterFeeds.get(0).getRoomType().getDescription());
        feedResponseDto1.setRoomSize(afterFeeds.get(0).getRoomSize().toString());
        feedResponseDto1.setRoomSizeName(afterFeeds.get(0).getRoomSize().getDescription());
        feedResponseDto1.setRoomCount(afterFeeds.get(0).getRoomCount().toString());
        feedResponseDto1.setRoomCountName(afterFeeds.get(0).getRoomCount().getDescription());
        feedResponseDto1.setRoomInfo(afterFeeds.get(0).getRoomInfo().toString());
        feedResponseDto1.setRoomInfoName(afterFeeds.get(0).getRoomInfo().getDescription());
        feedResponseDto1.setLocation(afterFeeds.get(0).getLocation().toString());
        feedResponseDto1.setLocationName(afterFeeds.get(0).getLocation().getDescription());

        List<FeedReplyResponseDto> feedReplyResponseDtos1 = new ArrayList<>();

        FeedReplyResponseDto feedReplyResponseDto1 = new FeedReplyResponseDto();

        feedReplyResponseDto1.setFeedReplyId(afterFeeds.get(0).getReplies().get(0).getFeedReplyId());
        feedReplyResponseDto1.setContent(afterFeeds.get(0).getReplies().get(0).getContent());
        feedReplyResponseDto1.setNickname(afterFeeds.get(0).getReplies().get(0).getMember().getNickname());
        feedReplyResponseDto1.setMemberId(afterFeeds.get(0).getReplies().get(0).getMember().getMemberId());
        feedReplyResponseDto1.setFeedId(afterFeeds.get(0).getReplies().get(0).getFeed().getFeedId());
        feedReplyResponseDto1.setCreatedDateTime(afterFeeds.get(0).getReplies().get(0).getCreatedDateTime());
        feedReplyResponseDto1.setModifiedDateTime(afterFeeds.get(0).getReplies().get(0).getModifiedDateTime());

        feedReplyResponseDtos1.add(feedReplyResponseDto1);

        feedResponseDto1.setReplies(feedReplyResponseDtos1);


        FeedResponseDto feedResponseDto2 = new FeedResponseDto();
        feedResponseDto2.setFeedId(afterFeeds.get(1).getFeedId());
        feedResponseDto2.setCreatedDateTime(afterFeeds.get(1).getCreatedDateTime());
        feedResponseDto2.setModifiedDateTime(afterFeeds.get(1).getModifiedDateTime());
        feedResponseDto2.setTitle(afterFeeds.get(1).getTitle());
        feedResponseDto2.setContent(afterFeeds.get(1).getContent());
        feedResponseDto2.setCoverPhoto(afterFeeds.get(1).getCoverPhoto());
        feedResponseDto2.setMemberId(afterFeeds.get(1).getMember().getMemberId());
        feedResponseDto2.setNickname(afterFeeds.get(1).getMember().getNickname());
        feedResponseDto2.setRoomType(afterFeeds.get(1).getRoomType().toString());
        feedResponseDto2.setRoomTypeName(afterFeeds.get(1).getRoomType().getDescription());
        feedResponseDto2.setRoomSize(afterFeeds.get(1).getRoomSize().toString());
        feedResponseDto2.setRoomSizeName(afterFeeds.get(1).getRoomSize().getDescription());
        feedResponseDto2.setRoomCount(afterFeeds.get(1).getRoomCount().toString());
        feedResponseDto2.setRoomCountName(afterFeeds.get(1).getRoomCount().getDescription());
        feedResponseDto2.setRoomInfo(afterFeeds.get(1).getRoomInfo().toString());
        feedResponseDto2.setRoomInfoName(afterFeeds.get(1).getRoomInfo().getDescription());
        feedResponseDto2.setLocation(afterFeeds.get(1).getLocation().toString());
        feedResponseDto2.setLocationName(afterFeeds.get(1).getLocation().getDescription());

        List<FeedReplyResponseDto> feedReplyResponseDtos2 = new ArrayList<>();

        FeedReplyResponseDto feedReplyResponseDto2 = new FeedReplyResponseDto();

        feedReplyResponseDto2.setFeedReplyId(afterFeeds.get(1).getReplies().get(0).getFeedReplyId());
        feedReplyResponseDto2.setContent(afterFeeds.get(1).getReplies().get(0).getContent());
        feedReplyResponseDto2.setNickname(afterFeeds.get(1).getReplies().get(0).getMember().getNickname());
        feedReplyResponseDto2.setMemberId(afterFeeds.get(1).getReplies().get(0).getMember().getMemberId());
        feedReplyResponseDto2.setFeedId(afterFeeds.get(1).getReplies().get(0).getFeed().getFeedId());
        feedReplyResponseDto2.setCreatedDateTime(afterFeeds.get(1).getReplies().get(0).getCreatedDateTime());
        feedReplyResponseDto2.setModifiedDateTime(afterFeeds.get(1).getReplies().get(0).getModifiedDateTime());

        feedReplyResponseDtos2.add(feedReplyResponseDto2);

        feedResponseDto2.setReplies(feedReplyResponseDtos2);

        List<FeedResponseDto> feedResponseDtos = new ArrayList<>();

        feedResponseDtos.add(feedResponseDto1);
        feedResponseDtos.add(feedResponseDto2);

        given(mapper.feedsToFeedResponseDtos(Mockito.anyList())).willReturn(feedResponseDtos);

        String searchString = "content";
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap();
        queryParams.add("page", "1");

        ResultActions actions =
                mockMvc.perform(
                        get("/feed/filter/{search-string}", searchString)
                                .params(queryParams)
                                .accept(MediaType.APPLICATION_JSON)
                );

        ResultActions result = actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].feedId").value(feed1.getFeedId()))
                .andExpect(jsonPath("$.data[0].title").value(feed1.getTitle()))
                .andExpect(jsonPath("$.data[0].memberId").value(feed1.getMember().getMemberId()))
                .andExpect(jsonPath("$.data[0].roomType").value(feed1.getRoomType().toString()))
                .andExpect(jsonPath("$.data[0].replies[0].content").value(feed1.getReplies().get(0).getContent()))
                .andExpect(jsonPath("$.data[0].replies[0].nickname").value(feed1.getReplies().get(0).getMember().getNickname()))
                .andExpect(jsonPath("$.data[1].title").value(feed2.getTitle()))
                .andExpect(jsonPath("$.data[1].content").value(feed2.getContent()))
                .andExpect(jsonPath("$.data[1].nickname").value(feed2.getMember().getNickname()))
                .andExpect(jsonPath("$.data[1].roomCount").value(feed2.getRoomCount().toString()))
                .andExpect(jsonPath("$.data[1].roomCountName").value(feed2.getRoomCount().getDescription()))
                .andExpect(jsonPath("$.data[1].replies[0].feedId").value(feed2.getReplies().get(0).getFeed().getFeedId()))
                .andExpect(jsonPath("$.data[1].replies[0].content").value(feed2.getReplies().get(0).getContent()));

    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void deleteFeedTest() throws Exception {
        long feedId = 1L;

        doNothing().when(feedService).deleteFeed(Mockito.anyLong());

        ResultActions actions = mockMvc.perform(
                delete("/feed/{feed-id}", feedId)
        );

        ResultActions result =
                actions.andExpect(status().isNoContent());

    }


}
