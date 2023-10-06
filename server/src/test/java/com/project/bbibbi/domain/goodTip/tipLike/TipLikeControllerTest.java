package com.project.bbibbi.domain.goodTip.tipLike;

import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import com.project.bbibbi.domain.goodTip.tipLike.dto.TipLikeRequestDto;
import com.project.bbibbi.domain.goodTip.tipLike.dto.TipLikeResponseDto;
import com.project.bbibbi.domain.goodTip.tipLike.entity.TipLike;
import com.project.bbibbi.domain.goodTip.tipLike.mapper.TipLikeMapper;
import com.project.bbibbi.domain.goodTip.tipLike.service.TipLikeService;
import com.project.bbibbi.domain.member.entity.Member;
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
import com.google.gson.Gson;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class TipLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private TipLikeService tipLikeService;

    @MockBean
    private TipLikeMapper mapper;

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
    void patchTipLikeTest() throws Exception {
        initTip();

        long loginMemberId = 2L;

        TipLikeRequestDto requestDto = new TipLikeRequestDto();
        requestDto.setMemberId(loginMemberId);
        requestDto.setTipId(tip1.getTipId());

        given(mapper.tipLikeRequestDtoToTipLike(Mockito.any(TipLikeRequestDto.class))).willReturn(new TipLike());

        TipLike tipLike = new TipLike();

        tipLike.setTipLikeId(1L);
        tipLike.setTip(tip1);
        tipLike.setMember(Member.builder().memberId(loginMemberId).build());
        tipLike.setCreatedDateTime(LocalDateTime.now());
        tipLike.setLikeYn(true);

        given(tipLikeService.settingTipLike(Mockito.any(TipLike.class))).willReturn(new TipLike());

        TipLikeResponseDto tipLikeResponseDto = new TipLikeResponseDto();

        tipLikeResponseDto.setTipId(tipLike.getTip().getTipId());
        tipLikeResponseDto.setMemberId(tipLike.getMember().getMemberId());
        tipLikeResponseDto.setLikeYn(tipLike.getLikeYn());

        given(mapper.tipLikeToTipLikeResponseDto(Mockito.any(TipLike.class))).willReturn(tipLikeResponseDto);

        ResultActions actions =
                mockMvc.perform(
                        patch("/tip/{tip-id}/tipLike", tip1.getTipId())
                                .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tipId").value(tipLike.getTip().getTipId()))
                .andExpect(jsonPath("$.data.memberId").value(tipLike.getMember().getMemberId()));

    }

}
