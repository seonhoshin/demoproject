package com.project.bbibbi.domain.follow;


import com.google.gson.Gson;
import com.project.bbibbi.domain.follow.dto.FollowListResponseDto;
import com.project.bbibbi.domain.follow.dto.FollowResponseDto;
import com.project.bbibbi.domain.follow.entity.Follow;
import com.project.bbibbi.domain.follow.mapper.FollowMapper;
import com.project.bbibbi.domain.follow.service.FollowService;
import com.project.bbibbi.domain.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

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
public class FollowControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private FollowService followService;

    @MockBean
    private FollowMapper mapper;

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void patchFollowTest() throws Exception {

        long fromMemberId = 1L;
        long toMemberId = 2L;

        Follow follow = new Follow();

        follow.setFromMember(Member.builder().memberId(fromMemberId).build());
        follow.setMember(Member.builder().memberId(toMemberId).build());
        follow.setFollowYn(true);
        follow.setCreatedDateTime(LocalDateTime.now());

        given(followService.settingFollow(Mockito.any(Follow.class))).willReturn(new Follow());

        FollowResponseDto followResponseDto = new FollowResponseDto();

        followResponseDto.setFromMemberId(follow.getFromMember().getMemberId());
        followResponseDto.setMemberId(follow.getMember().getMemberId());
        followResponseDto.setFollowYn(follow.getFollowYn());

        given(mapper.followToFollowResponseDto(Mockito.any(Follow.class))).willReturn(followResponseDto);

        ResultActions actions =
                mockMvc.perform(
                        patch("/follow/choose/{from-member-id}/{member-id}",fromMemberId,toMemberId)
                                .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fromMemberId").value(follow.getFromMember().getMemberId()))
                .andExpect(jsonPath("$.data.memberId").value(follow.getMember().getMemberId()))
                .andExpect(jsonPath("$.data.followYn").value(follow.getFollowYn()));

    }

    @Test
    void getFromFollowTest() throws Exception {

        long fromMemberId = 1L;
        long toMemberId1 = 2L;
        long toMemberId2 = 3L;

        Follow follow1 = new Follow();

        follow1.setFromMember(Member.builder().memberId(fromMemberId).build());
        follow1.setMember(Member.builder().memberId(toMemberId1).build());
        follow1.setFollowYn(true);
        follow1.setCreatedDateTime(LocalDateTime.now());

        Follow follow2 = new Follow();

        follow2.setFromMember(Member.builder().memberId(fromMemberId).build());
        follow2.setMember(Member.builder().memberId(toMemberId2).build());
        follow2.setFollowYn(true);
        follow2.setCreatedDateTime(LocalDateTime.now());

        List<Follow> followList = new ArrayList<>();
        followList.add(follow1);
        followList.add(follow2);

        given(followService.findFromFollow(Mockito.anyLong())).willReturn(new ArrayList<>());

        List<FollowListResponseDto> followListResponseDtoList = new ArrayList<>();

        for(Follow follow : followList){
            FollowListResponseDto followListResponseDto = new FollowListResponseDto();

            followListResponseDto.setFromMemberId(follow.getFromMember().getMemberId());
            followListResponseDto.setMemberId(follow.getMember().getMemberId());
            followListResponseDto.setFollowYn(follow.getFollowYn());

            followListResponseDtoList.add(followListResponseDto);
        }

        given(mapper.followsToFollowListResponseDtos(Mockito.any())).willReturn(followListResponseDtoList);

        ResultActions actions =
                mockMvc.perform(
                        get("/follow/from/{member-id}",fromMemberId)
                                .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].fromMemberId").value(follow1.getFromMember().getMemberId()))
                .andExpect(jsonPath("$.data[0].memberId").value(follow1.getMember().getMemberId()))
                .andExpect(jsonPath("$.data[1].memberId").value(follow2.getMember().getMemberId()))
                .andExpect(jsonPath("$.data[1].followYn").value(follow2.getFollowYn()));

    }

    @Test
    void getToFollowTest() throws Exception {

        long fromMemberId1 = 2L;
        long fromMemberId2 = 3L;
        long toMemberId = 1L;

        Follow follow1 = new Follow();

        follow1.setFromMember(Member.builder().memberId(fromMemberId1).build());
        follow1.setMember(Member.builder().memberId(toMemberId).build());
        follow1.setFollowYn(true);
        follow1.setCreatedDateTime(LocalDateTime.now());

        Follow follow2 = new Follow();

        follow2.setFromMember(Member.builder().memberId(fromMemberId2).build());
        follow2.setMember(Member.builder().memberId(toMemberId).build());
        follow2.setFollowYn(true);
        follow2.setCreatedDateTime(LocalDateTime.now());

        List<Follow> followList = new ArrayList<>();
        followList.add(follow1);
        followList.add(follow2);

        given(followService.findFromFollow(Mockito.anyLong())).willReturn(new ArrayList<>());

        List<FollowListResponseDto> followListResponseDtoList = new ArrayList<>();

        for(Follow follow : followList){
            FollowListResponseDto followListResponseDto = new FollowListResponseDto();

            followListResponseDto.setFromMemberId(follow.getFromMember().getMemberId());
            followListResponseDto.setMemberId(follow.getMember().getMemberId());
            followListResponseDto.setFollowYn(follow.getFollowYn());

            followListResponseDtoList.add(followListResponseDto);
        }

        given(mapper.followsToFollowListResponseDtos(Mockito.any())).willReturn(followListResponseDtoList);

        ResultActions actions =
                mockMvc.perform(
                        get("/follow/to/{member-id}",toMemberId)
                                .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].memberId").value(follow1.getMember().getMemberId()))
                .andExpect(jsonPath("$.data[0].fromMemberId").value(follow1.getFromMember().getMemberId()))
                .andExpect(jsonPath("$.data[1].fromMemberId").value(follow2.getFromMember().getMemberId()))
                .andExpect(jsonPath("$.data[1].followYn").value(follow2.getFollowYn()));

    }
}
