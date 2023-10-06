package com.project.bbibbi.domain.goodTip.tipReplyLike;

import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import com.project.bbibbi.domain.goodTip.tipLike.dto.TipLikeRequestDto;
import com.project.bbibbi.domain.goodTip.tipLike.dto.TipLikeResponseDto;
import com.project.bbibbi.domain.goodTip.tipLike.entity.TipLike;
import com.project.bbibbi.domain.goodTip.tipLike.mapper.TipLikeMapper;
import com.project.bbibbi.domain.goodTip.tipLike.service.TipLikeService;
import com.project.bbibbi.domain.goodTip.tipReply.entity.TipReply;
import com.project.bbibbi.domain.goodTip.tipReplyLike.dto.TipReplyLikeRequestDto;
import com.project.bbibbi.domain.goodTip.tipReplyLike.dto.TipReplyLikeResponseDto;
import com.project.bbibbi.domain.goodTip.tipReplyLike.entity.TipReplyLike;
import com.project.bbibbi.domain.goodTip.tipReplyLike.mapper.TipReplyLikeMapper;
import com.project.bbibbi.domain.goodTip.tipReplyLike.service.TipReplyLikeService;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.global.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import com.google.gson.Gson;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class TipReplyLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private TipReplyLikeService tipReplyLikeService;

    @MockBean
    private TipReplyLikeMapper mapper;

    private Tip tip1;

    private TipReply tipReply1;

    @BeforeEach
    public void initTipAndTipReply() throws Exception {
        tip1 = new Tip();

        tip1.setTipId(1L);
        tip1.setCreatedDateTime(LocalDateTime.now());
        tip1.setTitle("title1");
        tip1.setContent("content1");
        tip1.setViews(0);
        tip1.setCoverPhoto("coverPhoto1");
        tip1.setMember(Member.builder().memberId(1L).nickname("nickname1").build());

        tipReply1 = new TipReply();
        tipReply1.setTipReplyId(1L);
        tipReply1.setTip(tip1);
        tipReply1.setContent("RContent1");
        tipReply1.setMember(Member.builder().memberId(1L).nickname("nickname1").build());
        tipReply1.setCreatedDateTime(LocalDateTime.now());

        List<TipReply> tipReplyList = new ArrayList<>();
        tipReplyList.add(tipReply1);

        tip1.setReplies(tipReplyList);

    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void patchTipReplyLikeTest() throws Exception {
        initTipAndTipReply();

        long loginMemberId = 2L;

        TipReplyLikeRequestDto requestDto = new TipReplyLikeRequestDto();
        requestDto.setMemberId(loginMemberId);
        requestDto.setTipReplyId(tipReply1.getTipReplyId());

        given(mapper.tipReplyLikeRequestDtoToTipReplyLike(Mockito.any(TipReplyLikeRequestDto.class))).willReturn(new TipReplyLike());

        TipReplyLike tipReplyLike = new TipReplyLike();

        tipReplyLike.setTipReplyLikeId(1L);
        tipReplyLike.setTipReply(tipReply1);
        tipReplyLike.setMember(Member.builder().memberId(loginMemberId).build());
        tipReplyLike.setCreatedDateTime(LocalDateTime.now());
        tipReplyLike.setReplyLikeYn(true);

        given(tipReplyLikeService.settingTipReplyLike(Mockito.any(TipReplyLike.class))).willReturn(new TipReplyLike());

        TipReplyLikeResponseDto tipReplyLikeResponseDto = new TipReplyLikeResponseDto();

        tipReplyLikeResponseDto.setTipReplyId(tipReplyLike.getTipReply().getTipReplyId());
        tipReplyLikeResponseDto.setMemberId(tipReplyLike.getMember().getMemberId());
        tipReplyLikeResponseDto.setReplyLikeYn(tipReplyLike.getReplyLikeYn());

        given(mapper.tipReplyLikeToTipReplyLikeResponseDto(Mockito.any(TipReplyLike.class))).willReturn(tipReplyLikeResponseDto);

        ResultActions actions =
                mockMvc.perform(
                        patch("/tip/tipReply/{tip-reply-id}/tipReplyLike", tipReply1.getTipReplyId())
                                .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tipReplyId").value(tipReplyLike.getTipReply().getTipReplyId()))
                .andExpect(jsonPath("$.data.memberId").value(tipReplyLike.getMember().getMemberId()));

    }

}
