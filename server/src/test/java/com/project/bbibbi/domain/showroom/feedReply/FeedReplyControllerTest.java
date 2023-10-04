package com.project.bbibbi.domain.showroom.feedReply;

import com.google.gson.Gson;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.showroom.feed.dto.FeedPatchDto;
import com.project.bbibbi.domain.showroom.feed.dto.FeedPostDto;
import com.project.bbibbi.domain.showroom.feed.dto.FeedResponseDto;
import com.project.bbibbi.domain.showroom.feed.entity.Feed;
import com.project.bbibbi.domain.showroom.feed.mapper.FeedMapper;
import com.project.bbibbi.domain.showroom.feed.service.FeedService;
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
public class FeedReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private FeedReplyService feedReplyService;

    @MockBean
    private FeedReplyMapper mapper;

    private Feed feed1;


    @BeforeEach
    public void initFeed() throws Exception {
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
    }


    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void feedSaveTest() throws Exception {

        initFeed();

        FeedReplyRequestDto post = new FeedReplyRequestDto();

        post.setContent("RContent1");

        given(mapper.feedReplyRequestDtoToFeedReply(Mockito.any(FeedReplyRequestDto.class))).willReturn(new FeedReply());

        FeedReply feedReply = new FeedReply();
        feedReply.setFeedReplyId(1L);
        feedReply.setFeed(feed1);
        feedReply.setMember(Member.builder().memberId(1L).build());
        feedReply.setContent(post.getContent());
        feedReply.setCreatedDateTime(LocalDateTime.now());

        given(feedReplyService.replySave(Mockito.any(FeedReply.class))).willReturn(new FeedReply());

        FeedReplyResponseDto responseDto = new FeedReplyResponseDto();

        responseDto.setFeedReplyId(feedReply.getFeedReplyId());
        responseDto.setFeedId(feedReply.getFeed().getFeedId());
        responseDto.setMemberId(feedReply.getMember().getMemberId());
        responseDto.setContent(feedReply.getContent());
        responseDto.setCreatedDateTime(feedReply.getCreatedDateTime());

        given(mapper.feedReplyToFeedReplyResponseDto(Mockito.any(FeedReply.class))).willReturn(responseDto);

        URI uri = UriComponentsBuilder.newInstance().path("/feed/{feed-id}/feedReply").buildAndExpand(feed1.getFeedId()).toUri();

        String content = gson.toJson(post);

        ResultActions actions =
                mockMvc.perform(
                        post(uri)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.feedReplyId").value(feedReply.getFeedReplyId()))
                .andExpect(jsonPath("$.feedId").value(feedReply.getFeed().getFeedId()))
                .andExpect(jsonPath("$.memberId").value(feedReply.getMember().getMemberId()))
                .andExpect(jsonPath("$.content").value(feedReply.getContent()));

    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void updateFeedReplyTest() throws Exception {
        initFeed();

        long feedReplyId = 1L;

        FeedReplyRequestDto patch = new FeedReplyRequestDto();
        patch.setContent("RContent11");

        given(mapper.feedReplyRequestDtoToFeedReply(Mockito.any(FeedReplyRequestDto.class))).willReturn(new FeedReply());

        FeedReply feedReply = new FeedReply();
        feedReply.setFeedReplyId(feedReplyId);
        feedReply.setFeed(feed1);
        feedReply.setMember(Member.builder().memberId(1L).build());
        feedReply.setContent(patch.getContent());
        feedReply.setCreatedDateTime(LocalDateTime.now());
        feedReply.setModifiedDateTime(LocalDateTime.now());

        given(feedReplyService.updateReply(Mockito.anyLong(), Mockito.any(FeedReplyRequestDto.class))).willReturn(new FeedReply());

        FeedReplyResponseDto responseDto = new FeedReplyResponseDto();

        responseDto.setFeedReplyId(feedReply.getFeedReplyId());
        responseDto.setFeedId(feedReply.getFeed().getFeedId());
        responseDto.setMemberId(feedReply.getMember().getMemberId());
        responseDto.setContent(feedReply.getContent());
        responseDto.setCreatedDateTime(feedReply.getCreatedDateTime());
        responseDto.setModifiedDateTime(feedReply.getModifiedDateTime());

        given(mapper.feedReplyToFeedReplyResponseDto(Mockito.any(FeedReply.class))).willReturn(responseDto);

        String content = gson.toJson(patch);

        URI uri = UriComponentsBuilder.newInstance().path("/feed/"+feed1.getFeedId()+"/feedReply/"+feedReplyId).build().toUri();

        ResultActions actions =
                mockMvc.perform(
                        patch(uri)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.feedReplyId").value(feedReply.getFeedReplyId()))
                .andExpect(jsonPath("$.feedId").value(feedReply.getFeed().getFeedId()))
                .andExpect(jsonPath("$.memberId").value(feedReply.getMember().getMemberId()))
                .andExpect(jsonPath("$.content").value(feedReply.getContent()));

    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void deleteFeedReplyTest() throws Exception {
        long feedReplyId = 1L;

        doNothing().when(feedReplyService).deleteReply(Mockito.anyLong());

        ResultActions actions = mockMvc.perform(
                delete("/feed/{feed-id}/feedReply/{reply-id}",feed1.getFeedId(),feedReplyId)
        );

        ResultActions result =
                actions.andExpect(status().isOk());
    }

}
