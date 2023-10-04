package com.project.bbibbi.domain.showroom.feedComment;

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
import com.project.bbibbi.domain.showroom.feedComment.mapper.FeedCommentMapper;
import com.project.bbibbi.domain.showroom.feedComment.service.FeedCommentService;
import com.project.bbibbi.domain.showroom.feedReply.dto.FeedReplyRequestDto;
import com.project.bbibbi.domain.showroom.feedReply.dto.FeedReplyResponseDto;
import com.project.bbibbi.domain.showroom.feedReply.entity.FeedReply;
import com.project.bbibbi.domain.showroom.feedReply.mapper.FeedReplyMapper;
import com.project.bbibbi.domain.showroom.feedReply.service.FeedReplyService;
import com.project.bbibbi.global.entity.*;
import org.junit.jupiter.api.BeforeEach;
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
public class FeedCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private FeedCommentService feedCommentService;

    @MockBean
    private FeedCommentMapper mapper;

    private Feed feed1;

    private FeedReply feedReply1;

    @BeforeEach
    public void initFeedAndFeedReply() throws Exception {
        feed1 = new Feed();

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

        feedReply1 = new FeedReply();

        feedReply1.setFeedReplyId(1L);
        feedReply1.setFeed(feed1);
        feedReply1.setContent("RContent1");
        feedReply1.setMember(Member.builder().memberId(1L).nickname("nickname1").build());
        feedReply1.setCreatedDateTime(LocalDateTime.now());
        feedReply1.setModifiedDateTime(LocalDateTime.now());

        List<FeedReply> feedReplyList = new ArrayList<>();
        feedReplyList.add(feedReply1);

        feed1.setReplies(feedReplyList);

    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void createCommentTest() throws Exception {
        initFeedAndFeedReply();

        FeedCommentDto post = new FeedCommentDto();

        post.setContent("RContent1Comment");

        given(mapper.feedCommentDtoToFeedComment(Mockito.any(FeedCommentDto.class))).willReturn(new FeedComment());

        FeedComment feedComment = new FeedComment();
        feedComment.setFeedCommentId(1L);
        feedComment.setFeedReply(feedReply1);
        feedComment.setFeed(feed1);
        feedComment.setMember(Member.builder().memberId(1L).build());
        feedComment.setContent(post.getContent());
        feedComment.setCreatedDateTime(LocalDateTime.now());

        given(feedCommentService.saveComment(Mockito.any(FeedComment.class))).willReturn(new FeedComment());

        FeedCommentDto responseDto = new FeedCommentDto();

        responseDto.setFeedCommentId(feedComment.getFeedCommentId());
        responseDto.setFeedReplyId(feedComment.getFeedReply().getFeedReplyId());
        responseDto.setFeedId(feedComment.getFeed().getFeedId());
        responseDto.setMemberId(feedComment.getMember().getMemberId());
        responseDto.setContent(feedComment.getContent());
        responseDto.setCreatedDateTime(feedComment.getCreatedDateTime());

        given(mapper.feedCommentToFeedCommentDto(Mockito.any(FeedComment.class))).willReturn(responseDto);

        URI uri = UriComponentsBuilder.newInstance().path("/feed/{feed-id}/feedReply/{feed-reply-id}/feedComment")
                .buildAndExpand(feed1.getFeedId(),feedReply1.getFeedReplyId())
                .toUri();

        String content = gson.toJson(post);

        ResultActions actions =
                mockMvc.perform(
                        post(uri)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.feedCommentId").value(feedComment.getFeedCommentId()))
                .andExpect(jsonPath("$.data.feedReplyId").value(feedComment.getFeedReply().getFeedReplyId()))
                .andExpect(jsonPath("$.data.feedId").value(feedComment.getFeed().getFeedId()))
                .andExpect(jsonPath("$.data.memberId").value(feedComment.getMember().getMemberId()))
                .andExpect(jsonPath("$.data.content").value(feedComment.getContent()));

    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void updateCommentTest() throws Exception {

        initFeedAndFeedReply();

        long feedCommentId = 1L;

        FeedCommentDto patch = new FeedCommentDto();

        patch.setContent("RContent1Comment11");

        given(mapper.feedCommentDtoToFeedComment(Mockito.any(FeedCommentDto.class))).willReturn(new FeedComment());

        FeedComment feedComment = new FeedComment();
        feedComment.setFeedCommentId(feedCommentId);
        feedComment.setFeedReply(feedReply1);
        feedComment.setFeed(feed1);
        feedComment.setMember(Member.builder().memberId(1L).build());
        feedComment.setContent(patch.getContent());
        feedComment.setCreatedDateTime(LocalDateTime.now());

        given(feedCommentService.updateComment(Mockito.anyLong(), Mockito.any(FeedCommentDto.class))).willReturn(new FeedComment());

        FeedCommentDto responseDto = new FeedCommentDto();

        responseDto.setFeedCommentId(feedComment.getFeedCommentId());
        responseDto.setFeedReplyId(feedComment.getFeedReply().getFeedReplyId());
        responseDto.setFeedId(feedComment.getFeed().getFeedId());
        responseDto.setMemberId(feedComment.getMember().getMemberId());
        responseDto.setContent(feedComment.getContent());
        responseDto.setCreatedDateTime(feedComment.getCreatedDateTime());

        given(mapper.feedCommentToFeedCommentDto(Mockito.any(FeedComment.class))).willReturn(responseDto);

        URI uri = UriComponentsBuilder.newInstance().path("/feed/{feed-id}/feedReply/{feed-reply-id}/feedComment/{feed-comment-id}")
                .buildAndExpand(feed1.getFeedId(),feedReply1.getFeedReplyId(),feedCommentId)
                .toUri();

        String content = gson.toJson(patch);

        ResultActions actions =
                mockMvc.perform(
                        patch(uri)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.feedCommentId").value(feedComment.getFeedCommentId()))
                .andExpect(jsonPath("$.data.feedReplyId").value(feedComment.getFeedReply().getFeedReplyId()))
                .andExpect(jsonPath("$.data.feedId").value(feedComment.getFeed().getFeedId()))
                .andExpect(jsonPath("$.data.memberId").value(feedComment.getMember().getMemberId()))
                .andExpect(jsonPath("$.data.content").value(feedComment.getContent()));


    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void deleteCommentTest() throws Exception {
        long feedCommentId = 1L;

        doNothing().when(feedCommentService).deleteComment(Mockito.anyLong());

        ResultActions actions = mockMvc.perform(
                delete("/feed/{feed-id}/feedReply/{feed-reply-id}/feedComment/{feed-comment-id}"
                        , feed1.getFeedId(), feedReply1.getFeedReplyId(), feedCommentId)
        );

        ResultActions result =
                actions.andExpect(status().isNoContent());
    }

}
