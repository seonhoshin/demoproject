package com.project.bbibbi.auth;

import com.google.gson.Gson;
import com.project.bbibbi.domain.member.controller.dto.MemberCreateApiRequest;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.member.service.dto.request.MemberCreateServiceRequest;
import com.project.bbibbi.global.entity.Role;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import com.project.bbibbi.domain.member.service.MemberService;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private MemberService memberService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void signupTest() throws Exception {

        MemberCreateApiRequest memberCreateApiRequest = new MemberCreateApiRequest("test@test.com","test1", "test1!");

        Member member1 = Member.builder().memberId(1L).email("test@test.com").nickname("test1").password(passwordEncoder.encode("test1!")).role(Role.USER).build();

        Long member1Id = member1.getMemberId();

        given(memberService.signup(Mockito.any(MemberCreateServiceRequest.class))).willReturn(member1Id);

        String content = gson.toJson(memberCreateApiRequest);

        ResultActions actions =
                mockMvc.perform(
                        post("/auth/signup")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions
                .andExpect(status().isCreated());

    }


}
