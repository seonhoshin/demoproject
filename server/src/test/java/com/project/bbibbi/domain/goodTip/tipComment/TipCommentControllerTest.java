package com.project.bbibbi.domain.goodTip.tipComment;


import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import com.project.bbibbi.domain.goodTip.tipComment.dto.TipCommentDto;
import com.project.bbibbi.domain.goodTip.tipComment.entity.TipComment;
import com.project.bbibbi.domain.goodTip.tipComment.mapper.TipCommentMapper;
import com.project.bbibbi.domain.goodTip.tipComment.service.TipCommentService;
import com.project.bbibbi.domain.goodTip.tipReply.entity.TipReply;
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
public class TipCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private TipCommentService tipCommentService;

    @MockBean
    private TipCommentMapper mapper;

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
    void createCommentTest() throws Exception {
        initTipAndTipReply();

        TipCommentDto post = new TipCommentDto();

        post.setContent("RContent1Comment");

        given(mapper.tipCommentDtoToTipComment(Mockito.any(TipCommentDto.class))).willReturn(new TipComment());

        TipComment tipComment = new TipComment();
        tipComment.setTipCommentId(1L);
        tipComment.setTipReply(tipReply1);
        tipComment.setTip(tip1);
        tipComment.setMember(Member.builder().memberId(1L).build());
        tipComment.setContent(post.getContent());
        tipComment.setCreatedDateTime(LocalDateTime.now());

        given(tipCommentService.saveComment(Mockito.any(TipComment.class))).willReturn(new TipComment());

        TipCommentDto responseDto = new TipCommentDto();

        responseDto.setTipCommentId(tipComment.getTipCommentId());
        responseDto.setTipReplyId(tipComment.getTipReply().getTipReplyId());
        responseDto.setTipId(tipComment.getTip().getTipId());
        responseDto.setMemberId(tipComment.getMember().getMemberId());
        responseDto.setContent(tipComment.getContent());
        responseDto.setCreatedDateTime(tipComment.getCreatedDateTime());

        given(mapper.tipCommentToTipCommentDto(Mockito.any(TipComment.class))).willReturn(responseDto);

        URI uri = UriComponentsBuilder.newInstance().path("/tip/{tip-id}/tipReply/{tip-reply-id}/tipComment")
                .buildAndExpand(tip1.getTipId(), tipReply1.getTipReplyId()).toUri();

        String content = gson.toJson(post);

        ResultActions actions =
                mockMvc.perform(
                        post(uri)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.tipCommentId").value(tipComment.getTipCommentId()))
                .andExpect(jsonPath("$.data.tipReplyId").value(tipComment.getTipReply().getTipReplyId()))
                .andExpect(jsonPath("$.data.tipId").value(tipComment.getTip().getTipId()))
                .andExpect(jsonPath("$.data.memberId").value(tipComment.getMember().getMemberId()))
                .andExpect(jsonPath("$.data.content").value(tipComment.getContent()));

    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void updateCommentTest() throws Exception {

        initTipAndTipReply();

        long tipCommentId = 1L;

        TipCommentDto patch = new TipCommentDto();

        patch.setContent("RContent1Comment11");

        given(mapper.tipCommentDtoToTipComment(Mockito.any(TipCommentDto.class))).willReturn(new TipComment());

        TipComment tipComment = new TipComment();
        tipComment.setTipCommentId(tipCommentId);
        tipComment.setTipReply(tipReply1);
        tipComment.setTip(tip1);
        tipComment.setMember(Member.builder().memberId(1L).build());
        tipComment.setContent(patch.getContent());
        tipComment.setCreatedDateTime(LocalDateTime.now());

        given(tipCommentService.updateComment(Mockito.anyLong(), Mockito.any(TipCommentDto.class))).willReturn(new TipComment());

        TipCommentDto responseDto = new TipCommentDto();

        responseDto.setTipCommentId(tipComment.getTipCommentId());
        responseDto.setTipReplyId(tipComment.getTipReply().getTipReplyId());
        responseDto.setTipId(tipComment.getTip().getTipId());
        responseDto.setMemberId(tipComment.getMember().getMemberId());
        responseDto.setContent(tipComment.getContent());
        responseDto.setCreatedDateTime(LocalDateTime.now());

        given(mapper.tipCommentToTipCommentDto(Mockito.any(TipComment.class))).willReturn(responseDto);

        URI uri = UriComponentsBuilder.newInstance().path(
                "/tip/{tip-id}/tipReply/{tip-reply-id}/tipComment/{comment-id}"
        ).buildAndExpand(tip1.getTipId(), tipReply1.getTipReplyId(), tipCommentId).toUri();

        String content = gson.toJson(patch);

        ResultActions actions =
                mockMvc.perform(
                        patch(uri)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tipCommentId").value(tipComment.getTipCommentId()))
                .andExpect(jsonPath("$.data.tipReplyId").value(tipComment.getTipReply().getTipReplyId()))
                .andExpect(jsonPath("$.data.tipId").value(tipComment.getTip().getTipId()))
                .andExpect(jsonPath("$.data.memberId").value(tipComment.getMember().getMemberId()))
                .andExpect(jsonPath("$.data.content").value(tipComment.getContent()));

    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void deleteCommentTest() throws Exception {
        long tipCommentId = 1L;

        doNothing().when(tipCommentService).deleteComment(Mockito.anyLong());

        ResultActions actions = mockMvc.perform(
                delete("/tip/{tip-id}/tipReply/{tip-reply-id}/tipComment/{tip-comment-id}"
                , tip1.getTipId(), tipReply1.getTipReplyId(), tipCommentId)
        );

        ResultActions result =
                actions.andExpect(status().isNoContent());

    }
}
