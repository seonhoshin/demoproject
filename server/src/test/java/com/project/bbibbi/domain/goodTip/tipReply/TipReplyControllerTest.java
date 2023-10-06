package com.project.bbibbi.domain.goodTip.tipReply;

import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import com.project.bbibbi.domain.goodTip.tipReply.dto.TipReplyRequestDto;
import com.project.bbibbi.domain.goodTip.tipReply.dto.TipReplyResponseDto;
import com.project.bbibbi.domain.goodTip.tipReply.entity.TipReply;
import com.project.bbibbi.domain.goodTip.tipReply.mapper.TipReplyMapper;
import com.project.bbibbi.domain.goodTip.tipReply.service.TipReplyService;
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

import javax.xml.transform.Result;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class TipReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private TipReplyService tipReplyService;

    @MockBean
    private TipReplyMapper mapper;

    private Tip tip1;

    @BeforeEach
    public void initTip() throws Exception {
        tip1 = new Tip();

        tip1.setTipId(1L);
        tip1.setCreatedDateTime(LocalDateTime.now());
        tip1.setTitle("title1");
        tip1.setContent("content1");
        tip1.setViews(0);
        tip1.setCoverPhoto("coverphoto1");
        tip1.setMember(Member.builder().memberId(1L).nickname("nickname1").build());

    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void tipSaveTest() throws Exception {
        initTip();

        TipReplyRequestDto post = new TipReplyRequestDto();

        post.setContent("RContent1");

        given(mapper.tipReplyRequestDtoToTipReply(Mockito.any(TipReplyRequestDto.class))).willReturn(new TipReply());

        TipReply tipReply = new TipReply();
        tipReply.setTipReplyId(1L);
        tipReply.setTip(tip1);
        tipReply.setMember(Member.builder().memberId(1L).build());
        tipReply.setContent(post.getContent());
        tipReply.setCreatedDateTime(LocalDateTime.now());

        given(tipReplyService.replySave(Mockito.any(TipReply.class))).willReturn(new TipReply());

        TipReplyResponseDto responseDto = new TipReplyResponseDto();

        responseDto.setTipReplyId(tipReply.getTipReplyId());
        responseDto.setTipId(tipReply.getTip().getTipId());
        responseDto.setMemberId(tipReply.getMember().getMemberId());
        responseDto.setContent(tipReply.getContent());
        responseDto.setCreatedDateTime(tipReply.getCreatedDateTime());

        given(mapper.tipReplyToTipReplyResponseDto(Mockito.any(TipReply.class))).willReturn(responseDto);

        URI uri = UriComponentsBuilder.newInstance().path("/tip/{tip-id}/tipReply").buildAndExpand(tip1.getTipId()).toUri();

        String content = gson.toJson(post);

        ResultActions actions =
                mockMvc.perform(
                        post(uri)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.tipReplyId").value(tipReply.getTipReplyId()))
                .andExpect(jsonPath("$.data.tipId").value(tipReply.getTip().getTipId()))
                .andExpect(jsonPath("$.data.memberId").value(tipReply.getMember().getMemberId()))
                .andExpect(jsonPath("$.data.content").value(tipReply.getContent()));

    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void updateTipReplyTest() throws Exception {
        initTip();

        long tipReplyId = 1L;

        TipReplyRequestDto patch = new TipReplyRequestDto();
        patch.setContent("RContent11");

        given(mapper.tipReplyRequestDtoToTipReply(Mockito.any(TipReplyRequestDto.class))).willReturn(new TipReply());

        TipReply tipReply = new TipReply();
        tipReply.setTipReplyId(tipReplyId);
        tipReply.setTip(tip1);
        tipReply.setMember(Member.builder().memberId(1L).build());
        tipReply.setContent(patch.getContent());
        tipReply.setCreatedDateTime(LocalDateTime.now());

        given(tipReplyService.updateReply(Mockito.anyLong(), Mockito.any(TipReplyRequestDto.class))).willReturn(new TipReply());

        TipReplyResponseDto responseDto = new TipReplyResponseDto();

        responseDto.setTipReplyId(tipReply.getTipReplyId());
        responseDto.setTipId(tipReply.getTip().getTipId());
        responseDto.setMemberId(tipReply.getMember().getMemberId());
        responseDto.setContent(tipReply.getContent());
        responseDto.setCreatedDateTime(tipReply.getCreatedDateTime());

        given(mapper.tipReplyToTipReplyResponseDto(Mockito.any(TipReply.class))).willReturn(responseDto);

        String content = gson.toJson(patch);

        URI uri = UriComponentsBuilder.newInstance().path("/tip/{tip-id}/tipReply/{tip-reply-id}")
                .buildAndExpand(tip1.getTipId(), tipReplyId).toUri();

        ResultActions actions =
                mockMvc.perform(
                        patch(uri)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tipReplyId").value(tipReply.getTipReplyId()))
                .andExpect(jsonPath("$.data.tipId").value(tipReply.getTip().getTipId()))
                .andExpect(jsonPath("$.data.memberId").value(tipReply.getMember().getMemberId()))
                .andExpect(jsonPath("$.data.content").value(tipReply.getContent()));

    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void deleteTipReplyTest() throws Exception {
        long tipReplyId = 1L;

        doNothing().when(tipReplyService).deleteReply(Mockito.anyLong());

        ResultActions actions = mockMvc.perform(
                delete("/tip/{tip-id}/tipReply/{reply-id}", tip1.getTipId(), tipReplyId)
        );

        ResultActions result =
                actions.andExpect(status().isNoContent());

    }

}
