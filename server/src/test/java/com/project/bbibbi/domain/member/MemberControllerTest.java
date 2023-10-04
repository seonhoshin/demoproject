package com.project.bbibbi.domain.member;

import com.google.gson.Gson;
import com.project.bbibbi.domain.member.controller.dto.MemberUpdateApiRequest;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.member.service.MemberService;
import com.project.bbibbi.domain.member.service.dto.request.MemberUpdateServiceRequest;
import com.project.bbibbi.global.entity.Role;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private MemberService memberService;


    @Test
    @WithMockUser(username = "test@test.com", roles = "USER") // username 은 아무거나 쓴다
    void patchMemberTest() throws Exception {

        long memberId = 1L;

        MemberUpdateApiRequest memberUpdateApiRequest1 =
                MemberUpdateApiRequest.builder().nickname("test2").myIntro("intro2").profileImg("profile2").build();

        Member patchedMember2 = Member.builder().memberId(1L)
                .email("test2@test.com")
                .nickname("test2")
                .myIntro("intro2")
                .profileImg("profile2")
                .role(Role.USER)
                .build();

        String content = gson.toJson(memberUpdateApiRequest1);

        doNothing().when(memberService).updateMember(Mockito.any(MemberUpdateServiceRequest.class));

        Gson gson = new Gson();

        URI uri = UriComponentsBuilder.newInstance().path("/members/{member-id}").buildAndExpand(memberId).toUri();

        ResultActions actions =
                mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(uri)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions.andExpect(status().isNoContent());

    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void deleteMemberTest() throws Exception {

        long memberId = 1L;

        doNothing().when(memberService).deleteMember(memberId);

        URI uri = UriComponentsBuilder.newInstance().path("/members/{member-id}").buildAndExpand(memberId).toUri();

        ResultActions actions = mockMvc.perform(delete(uri));

        actions.andExpect(status().isNoContent());
    }

}
