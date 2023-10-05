package com.project.bbibbi.domain.showroom.feedBookMark;

import com.google.gson.Gson;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.showroom.feed.dto.FeedPatchDto;
import com.project.bbibbi.domain.showroom.feed.dto.FeedPostDto;
import com.project.bbibbi.domain.showroom.feed.dto.FeedResponseDto;
import com.project.bbibbi.domain.showroom.feed.entity.Feed;
import com.project.bbibbi.domain.showroom.feed.mapper.FeedMapper;
import com.project.bbibbi.domain.showroom.feed.service.FeedService;
import com.project.bbibbi.domain.showroom.feedBookMark.dto.FeedBookMarkRequestDto;
import com.project.bbibbi.domain.showroom.feedBookMark.dto.FeedBookMarkResponseDto;
import com.project.bbibbi.domain.showroom.feedBookMark.entity.FeedBookMark;
import com.project.bbibbi.domain.showroom.feedBookMark.mapper.FeedBookMarkMapper;
import com.project.bbibbi.domain.showroom.feedBookMark.service.FeedBookMarkService;
import com.project.bbibbi.domain.showroom.feedComment.dto.FeedCommentDto;
import com.project.bbibbi.domain.showroom.feedComment.entity.FeedComment;
import com.project.bbibbi.domain.showroom.feedLike.dto.FeedLikeRequestDto;
import com.project.bbibbi.domain.showroom.feedLike.dto.FeedLikeResponseDto;
import com.project.bbibbi.domain.showroom.feedLike.entity.FeedLike;
import com.project.bbibbi.domain.showroom.feedLike.mapper.FeedLikeMapper;
import com.project.bbibbi.domain.showroom.feedLike.service.FeedLikeService;
import com.project.bbibbi.domain.showroom.feedReply.dto.FeedReplyResponseDto;
import com.project.bbibbi.domain.showroom.feedReply.entity.FeedReply;
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
public class FeedBookMarkControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private FeedBookMarkService feedBookMarkService;

    @MockBean
    private FeedBookMarkMapper mapper;

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
    void patchFeedBookMarkTest() throws Exception {
        initFeed();

        long loginMemberId = 2L;

        FeedBookMarkRequestDto requestDto = new FeedBookMarkRequestDto();
        requestDto.setMemberId(loginMemberId);
        requestDto.setFeedId(feed1.getFeedId());

        given(mapper.feedBookMarkRequestDtoToFeedBookMark(Mockito.any(FeedBookMarkRequestDto.class))).willReturn(new FeedBookMark());

        FeedBookMark feedBookMark = new FeedBookMark();

        feedBookMark.setFeedBookMarkId(1L);
        feedBookMark.setFeed(feed1);
        feedBookMark.setMember(Member.builder().memberId(loginMemberId).build());
        feedBookMark.setCreatedDateTime(LocalDateTime.now());
        feedBookMark.setBookMarkYn(true);

        given(feedBookMarkService.settingFeedBookMark(Mockito.any(FeedBookMark.class))).willReturn(new FeedBookMark());

        FeedBookMarkResponseDto feedBookMarkResponseDto = new FeedBookMarkResponseDto();

        feedBookMarkResponseDto.setFeedId(feedBookMark.getFeed().getFeedId());
        feedBookMarkResponseDto.setMemberId(feedBookMark.getMember().getMemberId());
        feedBookMarkResponseDto.setBookMarkYn(feedBookMark.getBookMarkYn());

        given(mapper.feedBookMarkToFeedBookMarkResponseDto(Mockito.any(FeedBookMark.class))).willReturn(feedBookMarkResponseDto);

        ResultActions actions =
                mockMvc.perform(
                        patch("/feed/{feed-id}/feedBookMark",feed1.getFeedId())
                                .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.feedId").value(feedBookMark.getFeed().getFeedId()))
                .andExpect(jsonPath("$.data.memberId").value(feedBookMark.getMember().getMemberId()));

    }
}
