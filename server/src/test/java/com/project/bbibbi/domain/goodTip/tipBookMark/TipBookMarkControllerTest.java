package com.project.bbibbi.domain.goodTip.tipBookMark;

import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import com.project.bbibbi.domain.goodTip.tipBookMark.dto.TipBookMarkRequestDto;
import com.project.bbibbi.domain.goodTip.tipBookMark.dto.TipBookMarkResponseDto;
import com.project.bbibbi.domain.goodTip.tipBookMark.entity.TipBookMark;
import com.project.bbibbi.domain.goodTip.tipBookMark.mapper.TipBookMarkMapper;
import com.project.bbibbi.domain.goodTip.tipBookMark.service.TipBookMarkService;
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
public class TipBookMarkControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private TipBookMarkService tipBookMarkService;

    @MockBean
    private TipBookMarkMapper mapper;

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
    void patchTipBookMarkTest() throws Exception {
        initTip();

        long loginMemberId = 2L;

        TipBookMarkRequestDto requestDto = new TipBookMarkRequestDto();
        requestDto.setMemberId(loginMemberId);
        requestDto.setTipId(tip1.getTipId());

        given(mapper.tipBookmarkRequestDtoToTipBookmark(Mockito.any(TipBookMarkRequestDto.class))).willReturn(new TipBookMark());

        TipBookMark tipBookMark = new TipBookMark();

        tipBookMark.setTipBookmarkId(1L);
        tipBookMark.setTip(tip1);
        tipBookMark.setMember(Member.builder().memberId(loginMemberId).build());
        tipBookMark.setCreatedDateTime(LocalDateTime.now());
        tipBookMark.setBookmarkYn(true);

        given(tipBookMarkService.settingTipBookmark(Mockito.any(TipBookMark.class))).willReturn(new TipBookMark());

        TipBookMarkResponseDto tipBookMarkResponseDto = new TipBookMarkResponseDto();

        tipBookMarkResponseDto.setTipId(tipBookMark.getTip().getTipId());
        tipBookMarkResponseDto.setMemberId(tipBookMark.getMember().getMemberId());
        tipBookMarkResponseDto.setBookmarkYn(tipBookMark.getBookmarkYn());

        given(mapper.tipBookmarkToTipBookmarkResponseDto(Mockito.any(TipBookMark.class))).willReturn(tipBookMarkResponseDto);

        ResultActions actions =
                mockMvc.perform(
                        patch("/tip/{tip-id}/tipBookMark", tip1.getTipId())
                                .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tipId").value(tipBookMark.getTip().getTipId()))
                .andExpect(jsonPath("$.data.memberId").value(tipBookMark.getMember().getMemberId()));

    }

}
