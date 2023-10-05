package com.project.bbibbi.domain.goodTip.tip;

import com.project.bbibbi.domain.goodTip.tip.dto.TipPatchDto;
import com.project.bbibbi.domain.goodTip.tip.dto.TipPostDto;
import com.project.bbibbi.domain.goodTip.tip.dto.TipResponseDto;
import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import com.project.bbibbi.domain.goodTip.tip.mapper.TipMapper;
import com.project.bbibbi.domain.goodTip.tip.service.TipService;
import com.project.bbibbi.domain.goodTip.tipComment.dto.TipCommentDto;
import com.project.bbibbi.domain.goodTip.tipComment.entity.TipComment;
import com.project.bbibbi.domain.goodTip.tipReply.dto.TipReplyResponseDto;
import com.project.bbibbi.domain.goodTip.tipReply.entity.TipReply;
import com.project.bbibbi.domain.goodTip.tipTag.dto.TagDto;
import com.project.bbibbi.domain.goodTip.tipTag.entity.Tag;
import com.project.bbibbi.domain.member.entity.Member;
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
import org.springframework.http.ResponseEntity;
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
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class TipControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private TipService tipService;

    @MockBean
    private TipMapper mapper;

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void createTipTest() throws Exception {
        TipPostDto post = new TipPostDto();
        post.setTitle("title1");
        post.setContent("content1");
        post.setCoverPhoto("coverphoto1");

        List<String> tagList = new ArrayList<>();
        tagList.add("tag1");
        tagList.add("tag2");
        post.setTagContents(tagList);

        String content = gson.toJson(post);

        given(mapper.tipPostDtoToTip(Mockito.any(TipPostDto.class))).willReturn(new Tip());

        given(tipService.createTip(Mockito.any(Tip.class))).willReturn(new Tip());

        TipResponseDto responseDto = new TipResponseDto();

        responseDto.setTipId(1L);
        responseDto.setTitle(post.getTitle());
        responseDto.setContent(post.getContent());
        responseDto.setCoverPhoto(post.getCoverPhoto());
        responseDto.setCreatedDateTime(LocalDateTime.now());

        TagDto tagDto1 = new TagDto();
        tagDto1.setTagId(1L);
        tagDto1.setTagContent(post.getTagContents().get(0));
        TagDto tagDto2 = new TagDto();
        tagDto2.setTagId(2L);
        tagDto2.setTagContent(post.getTagContents().get(1));
        List<TagDto> tagDtoList = new ArrayList<>();
        tagDtoList.add(tagDto1);
        tagDtoList.add(tagDto2);
        responseDto.setTags(tagDtoList);


        given(mapper.tipToTipResponseDto(Mockito.any(Tip.class))).willReturn(responseDto);

        ResultActions actions =
                mockMvc.perform(
                        post("/tip")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.tipId").value(responseDto.getTipId()))
                .andExpect(jsonPath("$.data.title").value(post.getTitle()))
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andExpect(jsonPath("$.data.coverPhoto").value(post.getCoverPhoto()))
                .andExpect(jsonPath("$.data.tags[0].tagContent").value(post.getTagContents().get(0)))
                .andExpect(jsonPath("$.data.tags[1].tagContent").value(post.getTagContents().get(1)));

    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void updateTipTest() throws Exception {
        long tipId = 1L;

        TipPatchDto patch = new TipPatchDto();
        patch.setTipId(tipId);
        patch.setTitle("title11");
        patch.setContent("content11");
        patch.setCoverPhoto("coverphoto11");

        List<String> tagList = new ArrayList<>();
        tagList.add("tag11");
        tagList.add("tag12");
        patch.setTagContents(tagList);

        String content = gson.toJson(patch);

        given(mapper.tipPatchDtoToTip(Mockito.any(TipPatchDto.class))).willReturn(new Tip());

        given(tipService.updateTip(Mockito.anyLong(), Mockito.any(Tip.class))).willReturn(new Tip());

        TipResponseDto tipResponseDto = new TipResponseDto();

        tipResponseDto.setTipId(patch.getTipId());
        tipResponseDto.setTitle(patch.getTitle());
        tipResponseDto.setContent(patch.getContent());
        tipResponseDto.setCoverPhoto(patch.getCoverPhoto());
        tipResponseDto.setCreatedDateTime(LocalDateTime.now());
        tipResponseDto.setModifiedDateTime(LocalDateTime.now());

        TagDto tagDto1 = new TagDto();
        tagDto1.setTagId(1L);
        tagDto1.setTagContent(patch.getTagContents().get(0));
        TagDto tagDto2 = new TagDto();
        tagDto2.setTagId(2L);
        tagDto2.setTagContent(patch.getTagContents().get(1));
        List<TagDto> tagDtoList = new ArrayList<>();
        tagDtoList.add(tagDto1);
        tagDtoList.add(tagDto2);
        tipResponseDto.setTags(tagDtoList);

        given(mapper.tipToTipResponseDto(Mockito.any(Tip.class))).willReturn(tipResponseDto);

        ResultActions actions =
                mockMvc.perform(
                        patch("/tip/{tip-id}", tipId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tipId").value(tipId))
                .andExpect(jsonPath("$.data.title").value(patch.getTitle()))
                .andExpect(jsonPath("$.data.content").value(patch.getContent()))
                .andExpect(jsonPath("$.data.coverPhoto").value(patch.getCoverPhoto()))
                .andExpect(jsonPath("$.data.tags[0].tagContent").value(patch.getTagContents().get(0)))
                .andExpect(jsonPath("$.data.tags[1].tagContent").value(patch.getTagContents().get(1)));

    }

    @Test
    void getTipTest() throws Exception {
        long tipId = 1L;

        Tip tip = new Tip();

        tip.setTipId(tipId);
        tip.setCreatedDateTime(LocalDateTime.now());
        tip.setModifiedDateTime(LocalDateTime.now());
        tip.setMember(Member.builder().memberId(1L).nickname("nickname1").build());
        tip.setTitle("title1");
        tip.setContent("content1");
        tip.setViews(0);

        List<Tag> tagList = new ArrayList<>();
        Tag tag1 = new Tag();
        tag1.setTagId(1L);
        tag1.setTagContent("tag1");
        tag1.setTip(tip);
        tag1.setCreatedDateTime(LocalDateTime.now());
        Tag tag2 = new Tag();
        tag2.setTagId(2L);
        tag2.setTagContent("tag2");
        tag2.setTip(tip);
        tag2.setCreatedDateTime(LocalDateTime.now());
        tagList.add(tag1);
        tagList.add(tag2);

        tip.setTags(tagList);

        List<TipReply> tipReplyList = new ArrayList<>();
        TipReply tipReply = new TipReply();
        tipReply.setTipReplyId(1L);
        tipReply.setContent("RContent1");
        tipReply.setTip(tip);
        tipReply.setMember(tip.getMember());
        tipReply.setCreatedDateTime(LocalDateTime.now());

        List<TipComment> tipCommentList = new ArrayList<>();
        TipComment tipComment = new TipComment();
        tipComment.setTipCommentId(1L);
        tipComment.setTip(tip);
        tipComment.setTipReply(tipReply);
        tipComment.setMember(tip.getMember());
        tipComment.setCreatedDateTime(LocalDateTime.now());
        tipComment.setContent("RContent1Comment1");
        tipComment.setParentComment(null);
        tipCommentList.add(tipComment);
        tipReply.setComments(tipCommentList);

        tipReplyList.add(tipReply);

        tip.setReplies(tipReplyList);

        given(tipService.getTip(Mockito.anyLong())).willReturn(new Tip());

        TipResponseDto tipResponseDto = new TipResponseDto();
        tipResponseDto.setTipId(tip.getTipId());
        tipResponseDto.setCreatedDateTime(tip.getCreatedDateTime());
        tipResponseDto.setModifiedDateTime(tip.getModifiedDateTime());
        tipResponseDto.setTitle(tip.getTitle());
        tipResponseDto.setContent(tip.getContent());
        tipResponseDto.setCoverPhoto(tip.getCoverPhoto());
        tipResponseDto.setMemberId(tip.getMember().getMemberId());
        tipResponseDto.setNickname(tip.getMember().getNickname());

        TagDto tagDto1 = new TagDto();
        tagDto1.setTagId(tip.getTags().get(0).getTagId());
        tagDto1.setTagContent(tip.getTags().get(0).getTagContent());
        TagDto tagDto2 = new TagDto();
        tagDto2.setTagId(tip.getTags().get(1).getTagId());
        tagDto2.setTagContent(tip.getTags().get(1).getTagContent());
        List<TagDto> tagDtoList = new ArrayList<>();
        tagDtoList.add(tagDto1);
        tagDtoList.add(tagDto2);
        tipResponseDto.setTags(tagDtoList);

        List<TipReplyResponseDto> tipReplyResponseDtoList = new ArrayList<>();

        TipReplyResponseDto tipReplyResponseDto = new TipReplyResponseDto();

        tipReplyResponseDto.setTipReplyId(tip.getReplies().get(0).getTipReplyId());
        tipReplyResponseDto.setContent(tip.getReplies().get(0).getContent());
        tipReplyResponseDto.setNickname(tip.getReplies().get(0).getMember().getNickname());
        tipReplyResponseDto.setMemberId(tip.getReplies().get(0).getMember().getMemberId());
        tipReplyResponseDto.setTipId(tip.getReplies().get(0).getTip().getTipId());
        tipReplyResponseDto.setCreatedDateTime(tip.getReplies().get(0).getCreatedDateTime());

        List<TipCommentDto> tipCommentDtoList = new ArrayList<>();
        TipCommentDto tipCommentDto = new TipCommentDto();

        tipCommentDto.setTipCommentId(tip.getReplies().get(0).getComments().get(0).getTipCommentId());
        tipCommentDto.setTipId(tip.getReplies().get(0).getComments().get(0).getTip().getTipId());
        tipCommentDto.setTipReplyId(tip.getReplies().get(0).getComments().get(0).getTipReply().getTipReplyId());
        tipCommentDto.setMemberId(tip.getReplies().get(0).getComments().get(0).getMember().getMemberId());
        tipCommentDto.setContent(tip.getReplies().get(0).getComments().get(0).getContent());
        tipCommentDto.setCreatedDateTime(tip.getReplies().get(0).getComments().get(0).getCreatedDateTime());

        tipCommentDtoList.add(tipCommentDto);
        tipReplyResponseDto.setComments(tipCommentDtoList);

        tipReplyResponseDtoList.add(tipReplyResponseDto);

        tipResponseDto.setReplies(tipReplyResponseDtoList);

        given(mapper.tipToTipResponseDto(Mockito.any(Tip.class))).willReturn(tipResponseDto);

        URI uri = UriComponentsBuilder.newInstance().path("/tip/{tip-id}").buildAndExpand(tipId).toUri();

        ResultActions actions =
                mockMvc.perform(
                        get("/tip/{tip-id}", tipId)
                                .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tipId").value(tip.getTipId()))
                .andExpect(jsonPath("$.data.title").value(tip.getTitle()))
                .andExpect(jsonPath("$.data.content").value(tip.getContent()))
                .andExpect(jsonPath("$.data.coverPhoto").value(tip.getCoverPhoto()))
                .andExpect(jsonPath("$.data.tags[0].tagContent").value(tip.getTags().get(0).getTagContent()))
                .andExpect(jsonPath("$.data.tags[1].tagContent").value(tip.getTags().get(1).getTagContent()))
                .andExpect(jsonPath("$.data.replies[0].tipReplyId").value(tip.getReplies().get(0).getTipReplyId()))
                .andExpect(jsonPath("$.data.replies[0].content").value(tip.getReplies().get(0).getContent()))
                .andExpect(jsonPath("$.data.replies[0].tipId").value(tip.getReplies().get(0).getTip().getTipId()))
                .andExpect(jsonPath("$.data.replies[0].nickname").value(tip.getReplies().get(0).getMember().getNickname()))
                .andExpect(jsonPath("$.data.replies[0].comments[0].tipCommentId").value(tip.getReplies().get(0).getComments().get(0).getTipCommentId()))
                .andExpect(jsonPath("$.data.replies[0].comments[0].tipReplyId").value(tip.getReplies().get(0).getComments().get(0).getTipReply().getTipReplyId()))
                .andExpect(jsonPath("$.data.replies[0].comments[0].content").value(tip.getReplies().get(0).getComments().get(0).getContent()));

    }

    @Test
    void getAllTipsTest() throws Exception {

        Tip tip1 = new Tip();

        tip1.setTipId(1L);
        tip1.setCreatedDateTime(LocalDateTime.now());
        tip1.setModifiedDateTime(LocalDateTime.now());
        tip1.setMember(Member.builder().memberId(1L).nickname("nickname1").build());
        tip1.setTitle("title1");
        tip1.setContent("content1");
        tip1.setViews(0);
        tip1.setCoverPhoto("coverphoto1");

        List<Tag> tagList = new ArrayList<>();
        Tag tag1 = new Tag();
        tag1.setTagId(1L);
        tag1.setTagContent("tag1");
        tag1.setTip(tip1);
        tag1.setCreatedDateTime(LocalDateTime.now());
        Tag tag2 = new Tag();
        tag2.setTagId(2L);
        tag2.setTagContent("tag2");
        tag2.setTip(tip1);
        tag2.setCreatedDateTime(LocalDateTime.now());
        tagList.add(tag1);
        tagList.add(tag2);

        tip1.setTags(tagList);

        List<TipReply> tipReplyList1 = new ArrayList<>();
        TipReply tipReply1 = new TipReply();
        tipReply1.setTipReplyId(1L);
        tipReply1.setContent("RContent1");
        tipReply1.setTip(tip1);
        tipReply1.setMember(tip1.getMember());
        tipReply1.setCreatedDateTime(LocalDateTime.now());
        tipReplyList1.add(tipReply1);

        tip1.setReplies(tipReplyList1);

        //////////////////////

        Tip tip2 = new Tip();

        tip2.setTipId(2L);
        tip2.setCreatedDateTime(LocalDateTime.now());
        tip2.setModifiedDateTime(LocalDateTime.now());
        tip2.setMember(Member.builder().memberId(1L).nickname("nickname1").build());
        tip2.setTitle("title2");
        tip2.setContent("content2");
        tip2.setViews(0);
        tip2.setCoverPhoto("coverphoto2");

        List<Tag> tagList2 = new ArrayList<>();
        Tag tag3 = new Tag();
        tag3.setTagId(3L);
        tag3.setTagContent("tag3");
        tag3.setTip(tip2);
        tag3.setCreatedDateTime(LocalDateTime.now());
        Tag tag4 = new Tag();
        tag4.setTagId(4L);
        tag4.setTagContent("tag4");
        tag4.setTip(tip2);
        tag4.setCreatedDateTime(LocalDateTime.now());
        tagList2.add(tag3);
        tagList2.add(tag4);

        tip2.setTags(tagList2);

        List<TipReply> tipReplyList2 = new ArrayList<>();
        TipReply tipReply2 = new TipReply();
        tipReply2.setTipReplyId(2L);
        tipReply2.setContent("RContent2");
        tipReply2.setTip(tip2);
        tipReply2.setMember(tip2.getMember());
        tipReply2.setCreatedDateTime(LocalDateTime.now());
        tipReplyList2.add(tipReply2);

        tip2.setReplies(tipReplyList2);

        List<Tip> tipList = new ArrayList<>();
        tipList.add(tip1);
        tipList.add(tip2);

        Page<Tip> pageTips = new PageImpl<>(tipList);

        given(tipService.getAllTips(Mockito.anyInt(), Mockito.anyInt())).willReturn(pageTips);

        List<Tip> afterTips = pageTips.getContent();

        TipResponseDto tipResponseDto1 = new TipResponseDto();
        tipResponseDto1.setTipId(afterTips.get(0).getTipId());
        tipResponseDto1.setCreatedDateTime(afterTips.get(0).getCreatedDateTime());
        tipResponseDto1.setTitle(afterTips.get(0).getTitle());
        tipResponseDto1.setContent(afterTips.get(0).getContent());
        tipResponseDto1.setCoverPhoto(afterTips.get(0).getCoverPhoto());
        tipResponseDto1.setMemberId(afterTips.get(0).getMember().getMemberId());
        tipResponseDto1.setNickname(afterTips.get(0).getMember().getNickname());

        TagDto tagDto1 = new TagDto();
        tagDto1.setTagId(tip1.getTags().get(0).getTagId());
        tagDto1.setTagContent(tip1.getTags().get(0).getTagContent());
        TagDto tagDto2 = new TagDto();
        tagDto2.setTagId(tip1.getTags().get(1).getTagId());
        tagDto2.setTagContent(tip1.getTags().get(1).getTagContent());
        List<TagDto> tagDtoList = new ArrayList<>();
        tagDtoList.add(tagDto1);
        tagDtoList.add(tagDto2);
        tipResponseDto1.setTags(tagDtoList);

        List<TipReplyResponseDto> tipReplyResponseDtoList = new ArrayList<>();

        TipReplyResponseDto tipReplyResponseDto = new TipReplyResponseDto();

        tipReplyResponseDto.setTipReplyId(afterTips.get(0).getReplies().get(0).getTipReplyId());
        tipReplyResponseDto.setContent(afterTips.get(0).getReplies().get(0).getContent());
        tipReplyResponseDto.setNickname(afterTips.get(0).getReplies().get(0).getMember().getNickname());
        tipReplyResponseDto.setMemberId(afterTips.get(0).getReplies().get(0).getMember().getMemberId());
        tipReplyResponseDto.setTipId(afterTips.get(0).getReplies().get(0).getTip().getTipId());
        tipReplyResponseDto.setCreatedDateTime(afterTips.get(0).getReplies().get(0).getCreatedDateTime());

        tipReplyResponseDtoList.add(tipReplyResponseDto);

        tipResponseDto1.setReplies(tipReplyResponseDtoList);

        TipResponseDto tipResponseDto2 = new TipResponseDto();
        tipResponseDto2.setTipId(afterTips.get(1).getTipId());
        tipResponseDto2.setCreatedDateTime(afterTips.get(1).getCreatedDateTime());
        tipResponseDto2.setTitle(afterTips.get(1).getTitle());
        tipResponseDto2.setContent(afterTips.get(1).getContent());
        tipResponseDto2.setCoverPhoto(afterTips.get(1).getCoverPhoto());
        tipResponseDto2.setMemberId(afterTips.get(1).getMember().getMemberId());
        tipResponseDto2.setNickname(afterTips.get(1).getMember().getNickname());

        TagDto tagDto3 = new TagDto();
        tagDto3.setTagId(tip2.getTags().get(0).getTagId());
        tagDto3.setTagContent(tip2.getTags().get(0).getTagContent());
        TagDto tagDto4 = new TagDto();
        tagDto4.setTagId(tip2.getTags().get(1).getTagId());
        tagDto4.setTagContent(tip2.getTags().get(1).getTagContent());
        List<TagDto> tagDtoList2 = new ArrayList<>();
        tagDtoList2.add(tagDto3);
        tagDtoList2.add(tagDto4);
        tipResponseDto2.setTags(tagDtoList2);

        List<TipReplyResponseDto> tipReplyResponseDtoList2 = new ArrayList<>();

        TipReplyResponseDto tipReplyResponseDto2 = new TipReplyResponseDto();

        tipReplyResponseDto2.setTipReplyId(afterTips.get(1).getReplies().get(0).getTipReplyId());
        tipReplyResponseDto2.setContent(afterTips.get(1).getReplies().get(0).getContent());
        tipReplyResponseDto2.setNickname(afterTips.get(1).getReplies().get(0).getMember().getNickname());
        tipReplyResponseDto2.setMemberId(afterTips.get(1).getReplies().get(0).getMember().getMemberId());
        tipReplyResponseDto2.setTipId(afterTips.get(1).getReplies().get(0).getTip().getTipId());
        tipReplyResponseDto2.setCreatedDateTime(afterTips.get(1).getReplies().get(0).getCreatedDateTime());

        tipReplyResponseDtoList2.add(tipReplyResponseDto2);

        tipResponseDto2.setReplies(tipReplyResponseDtoList2);

        List<TipResponseDto> tipResponseDtoList = new ArrayList<>();


//        given(mapper.tipToTipResponseDto(Mockito.any(Tip.class))).willReturn(tipResponseDto2);
//        given(mapper.tipToTipResponseDto(Mockito.any(Tip.class))).willReturn(tipResponseDto1);

        given(mapper.tipToTipResponseDto(Mockito.any(Tip.class))).willReturn(tipResponseDto2);

        tipResponseDtoList.add(tipResponseDto1);
        tipResponseDtoList.add(tipResponseDto2);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", "1");

        ResultActions actions =
                mockMvc.perform(
                        get("/tip").params(queryParams)
                                .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[1].tipId").value(tip2.getTipId()))
                .andExpect(jsonPath("$.data[1].title").value(tip2.getTitle()))
                .andExpect(jsonPath("$.data[1].memberId").value(tip2.getMember().getMemberId()))
                .andExpect(jsonPath("$.data[1].nickname").value(tip2.getMember().getNickname()))
                .andExpect(jsonPath("$.data[1].content").value(tip2.getContent()))
                .andExpect(jsonPath("$.data[1].tags[0].tagContent").value(tip2.getTags().get(0).getTagContent()))
                .andExpect(jsonPath("$.data[1].tags[1].tagContent").value(tip2.getTags().get(1).getTagContent()))
                .andExpect(jsonPath("$.data[1].replies[0].tipId").value(tip2.getReplies().get(0).getTip().getTipId()))
                .andExpect(jsonPath("$.data[1].replies[0].content").value(tip2.getReplies().get(0).getContent()));

    }

    @Test
    void getAllSearchTipsTest() throws Exception {

        Tip tip1 = new Tip();

        tip1.setTipId(1L);
        tip1.setCreatedDateTime(LocalDateTime.now());
        tip1.setModifiedDateTime(LocalDateTime.now());
        tip1.setMember(Member.builder().memberId(1L).nickname("nickname1").build());
        tip1.setTitle("title1");
        tip1.setContent("content1");
        tip1.setViews(0);
        tip1.setCoverPhoto("coverphoto1");

        List<Tag> tagList = new ArrayList<>();
        Tag tag1 = new Tag();
        tag1.setTagId(1L);
        tag1.setTagContent("tag1");
        tag1.setTip(tip1);
        tag1.setCreatedDateTime(LocalDateTime.now());
        Tag tag2 = new Tag();
        tag2.setTagId(2L);
        tag2.setTagContent("tag2");
        tag2.setTip(tip1);
        tag2.setCreatedDateTime(LocalDateTime.now());
        tagList.add(tag1);
        tagList.add(tag2);

        tip1.setTags(tagList);

        List<TipReply> tipReplyList1 = new ArrayList<>();
        TipReply tipReply1 = new TipReply();
        tipReply1.setTipReplyId(1L);
        tipReply1.setContent("RContent1");
        tipReply1.setTip(tip1);
        tipReply1.setMember(tip1.getMember());
        tipReply1.setCreatedDateTime(LocalDateTime.now());
        tipReplyList1.add(tipReply1);

        tip1.setReplies(tipReplyList1);

        //////////////////////

        Tip tip2 = new Tip();

        tip2.setTipId(2L);
        tip2.setCreatedDateTime(LocalDateTime.now());
        tip2.setModifiedDateTime(LocalDateTime.now());
        tip2.setMember(Member.builder().memberId(1L).nickname("nickname1").build());
        tip2.setTitle("title2");
        tip2.setContent("content2");
        tip2.setViews(0);
        tip2.setCoverPhoto("coverphoto2");

        List<Tag> tagList2 = new ArrayList<>();
        Tag tag3 = new Tag();
        tag3.setTagId(3L);
        tag3.setTagContent("tag3");
        tag3.setTip(tip2);
        tag3.setCreatedDateTime(LocalDateTime.now());
        Tag tag4 = new Tag();
        tag4.setTagId(4L);
        tag4.setTagContent("tag4");
        tag4.setTip(tip2);
        tag4.setCreatedDateTime(LocalDateTime.now());
        tagList2.add(tag3);
        tagList2.add(tag4);

        tip2.setTags(tagList2);

        List<TipReply> tipReplyList2 = new ArrayList<>();
        TipReply tipReply2 = new TipReply();
        tipReply2.setTipReplyId(2L);
        tipReply2.setContent("RContent2");
        tipReply2.setTip(tip2);
        tipReply2.setMember(tip2.getMember());
        tipReply2.setCreatedDateTime(LocalDateTime.now());
        tipReplyList2.add(tipReply2);

        tip2.setReplies(tipReplyList2);

        List<Tip> tipList = new ArrayList<>();
        tipList.add(tip1);
        tipList.add(tip2);

        Page<Tip> pageTips = new PageImpl<>(tipList);

        given(tipService.getAllSearchTips(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).willReturn(tipList);

        List<Tip> afterTips = pageTips.getContent();

        TipResponseDto tipResponseDto1 = new TipResponseDto();
        tipResponseDto1.setTipId(afterTips.get(0).getTipId());
        tipResponseDto1.setCreatedDateTime(afterTips.get(0).getCreatedDateTime());
        tipResponseDto1.setTitle(afterTips.get(0).getTitle());
        tipResponseDto1.setContent(afterTips.get(0).getContent());
        tipResponseDto1.setCoverPhoto(afterTips.get(0).getCoverPhoto());
        tipResponseDto1.setMemberId(afterTips.get(0).getMember().getMemberId());
        tipResponseDto1.setNickname(afterTips.get(0).getMember().getNickname());

        TagDto tagDto1 = new TagDto();
        tagDto1.setTagId(tip1.getTags().get(0).getTagId());
        tagDto1.setTagContent(tip1.getTags().get(0).getTagContent());
        TagDto tagDto2 = new TagDto();
        tagDto2.setTagId(tip1.getTags().get(1).getTagId());
        tagDto2.setTagContent(tip1.getTags().get(1).getTagContent());
        List<TagDto> tagDtoList = new ArrayList<>();
        tagDtoList.add(tagDto1);
        tagDtoList.add(tagDto2);
        tipResponseDto1.setTags(tagDtoList);

        List<TipReplyResponseDto> tipReplyResponseDtoList = new ArrayList<>();

        TipReplyResponseDto tipReplyResponseDto = new TipReplyResponseDto();

        tipReplyResponseDto.setTipReplyId(afterTips.get(0).getReplies().get(0).getTipReplyId());
        tipReplyResponseDto.setContent(afterTips.get(0).getReplies().get(0).getContent());
        tipReplyResponseDto.setNickname(afterTips.get(0).getReplies().get(0).getMember().getNickname());
        tipReplyResponseDto.setMemberId(afterTips.get(0).getReplies().get(0).getMember().getMemberId());
        tipReplyResponseDto.setTipId(afterTips.get(0).getReplies().get(0).getTip().getTipId());
        tipReplyResponseDto.setCreatedDateTime(afterTips.get(0).getReplies().get(0).getCreatedDateTime());

        tipReplyResponseDtoList.add(tipReplyResponseDto);

        tipResponseDto1.setReplies(tipReplyResponseDtoList);

        TipResponseDto tipResponseDto2 = new TipResponseDto();
        tipResponseDto2.setTipId(afterTips.get(1).getTipId());
        tipResponseDto2.setCreatedDateTime(afterTips.get(1).getCreatedDateTime());
        tipResponseDto2.setTitle(afterTips.get(1).getTitle());
        tipResponseDto2.setContent(afterTips.get(1).getContent());
        tipResponseDto2.setCoverPhoto(afterTips.get(1).getCoverPhoto());
        tipResponseDto2.setMemberId(afterTips.get(1).getMember().getMemberId());
        tipResponseDto2.setNickname(afterTips.get(1).getMember().getNickname());

        TagDto tagDto3 = new TagDto();
        tagDto3.setTagId(tip2.getTags().get(0).getTagId());
        tagDto3.setTagContent(tip2.getTags().get(0).getTagContent());
        TagDto tagDto4 = new TagDto();
        tagDto4.setTagId(tip2.getTags().get(1).getTagId());
        tagDto4.setTagContent(tip2.getTags().get(1).getTagContent());
        List<TagDto> tagDtoList2 = new ArrayList<>();
        tagDtoList2.add(tagDto3);
        tagDtoList2.add(tagDto4);
        tipResponseDto2.setTags(tagDtoList2);

        List<TipReplyResponseDto> tipReplyResponseDtoList2 = new ArrayList<>();

        TipReplyResponseDto tipReplyResponseDto2 = new TipReplyResponseDto();

        tipReplyResponseDto2.setTipReplyId(afterTips.get(1).getReplies().get(0).getTipReplyId());
        tipReplyResponseDto2.setContent(afterTips.get(1).getReplies().get(0).getContent());
        tipReplyResponseDto2.setNickname(afterTips.get(1).getReplies().get(0).getMember().getNickname());
        tipReplyResponseDto2.setMemberId(afterTips.get(1).getReplies().get(0).getMember().getMemberId());
        tipReplyResponseDto2.setTipId(afterTips.get(1).getReplies().get(0).getTip().getTipId());
        tipReplyResponseDto2.setCreatedDateTime(afterTips.get(1).getReplies().get(0).getCreatedDateTime());

        tipReplyResponseDtoList2.add(tipReplyResponseDto2);

        tipResponseDto2.setReplies(tipReplyResponseDtoList2);

        List<TipResponseDto> tipResponseDtoList = new ArrayList<>();


//        given(mapper.tipToTipResponseDto(Mockito.any(Tip.class))).willReturn(tipResponseDto2);
//        given(mapper.tipToTipResponseDto(Mockito.any(Tip.class))).willReturn(tipResponseDto1);

        given(mapper.tipToTipResponseDto(Mockito.any(Tip.class))).willReturn(tipResponseDto2);

        tipResponseDtoList.add(tipResponseDto1);
        tipResponseDtoList.add(tipResponseDto2);

        String searchString = "content";
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", "1");

        ResultActions actions =
                mockMvc.perform(
                        get("/tip/search/{search-string}", searchString).params(queryParams)
                                .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[1].tipId").value(tip2.getTipId()))
                .andExpect(jsonPath("$.data[1].title").value(tip2.getTitle()))
                .andExpect(jsonPath("$.data[1].memberId").value(tip2.getMember().getMemberId()))
                .andExpect(jsonPath("$.data[1].nickname").value(tip2.getMember().getNickname()))
                .andExpect(jsonPath("$.data[1].content").value(tip2.getContent()))
                .andExpect(jsonPath("$.data[1].tags[0].tagContent").value(tip2.getTags().get(0).getTagContent()))
                .andExpect(jsonPath("$.data[1].tags[1].tagContent").value(tip2.getTags().get(1).getTagContent()))
                .andExpect(jsonPath("$.data[1].replies[0].tipId").value(tip2.getReplies().get(0).getTip().getTipId()))
                .andExpect(jsonPath("$.data[1].replies[0].content").value(tip2.getReplies().get(0).getContent()));

    }

    @Test
    void getAllSearchTipTagsTest() throws Exception {
        Tip tip1 = new Tip();

        tip1.setTipId(1L);
        tip1.setCreatedDateTime(LocalDateTime.now());
        tip1.setModifiedDateTime(LocalDateTime.now());
        tip1.setMember(Member.builder().memberId(1L).nickname("nickname1").build());
        tip1.setTitle("title1");
        tip1.setContent("content1");
        tip1.setViews(0);
        tip1.setCoverPhoto("coverphoto1");

        List<Tag> tagList = new ArrayList<>();
        Tag tag1 = new Tag();
        tag1.setTagId(1L);
        tag1.setTagContent("tag1");
        tag1.setTip(tip1);
        tag1.setCreatedDateTime(LocalDateTime.now());
        Tag tag2 = new Tag();
        tag2.setTagId(2L);
        tag2.setTagContent("tag2");
        tag2.setTip(tip1);
        tag2.setCreatedDateTime(LocalDateTime.now());
        tagList.add(tag1);
        tagList.add(tag2);

        tip1.setTags(tagList);

        List<TipReply> tipReplyList1 = new ArrayList<>();
        TipReply tipReply1 = new TipReply();
        tipReply1.setTipReplyId(1L);
        tipReply1.setContent("RContent1");
        tipReply1.setTip(tip1);
        tipReply1.setMember(tip1.getMember());
        tipReply1.setCreatedDateTime(LocalDateTime.now());
        tipReplyList1.add(tipReply1);

        tip1.setReplies(tipReplyList1);

        //////////////////////

        Tip tip2 = new Tip();

        tip2.setTipId(2L);
        tip2.setCreatedDateTime(LocalDateTime.now());
        tip2.setModifiedDateTime(LocalDateTime.now());
        tip2.setMember(Member.builder().memberId(1L).nickname("nickname1").build());
        tip2.setTitle("title2");
        tip2.setContent("content2");
        tip2.setViews(0);
        tip2.setCoverPhoto("coverphoto2");

        List<Tag> tagList2 = new ArrayList<>();
        Tag tag3 = new Tag();
        tag3.setTagId(3L);
        tag3.setTagContent("tag3");
        tag3.setTip(tip2);
        tag3.setCreatedDateTime(LocalDateTime.now());
        Tag tag4 = new Tag();
        tag4.setTagId(4L);
        tag4.setTagContent("tag4");
        tag4.setTip(tip2);
        tag4.setCreatedDateTime(LocalDateTime.now());
        tagList2.add(tag3);
        tagList2.add(tag4);

        tip2.setTags(tagList2);

        List<TipReply> tipReplyList2 = new ArrayList<>();
        TipReply tipReply2 = new TipReply();
        tipReply2.setTipReplyId(2L);
        tipReply2.setContent("RContent2");
        tipReply2.setTip(tip2);
        tipReply2.setMember(tip2.getMember());
        tipReply2.setCreatedDateTime(LocalDateTime.now());
        tipReplyList2.add(tipReply2);

        tip2.setReplies(tipReplyList2);

        List<Tip> tipList = new ArrayList<>();
        tipList.add(tip1);
        tipList.add(tip2);

        Page<Tip> pageTips = new PageImpl<>(tipList);

        given(tipService.getAllSearchTipTags(Mockito.anyString())).willReturn(tipList);

        List<Tip> afterTips = pageTips.getContent();

        TipResponseDto tipResponseDto1 = new TipResponseDto();
        tipResponseDto1.setTipId(afterTips.get(0).getTipId());
        tipResponseDto1.setCreatedDateTime(afterTips.get(0).getCreatedDateTime());
        tipResponseDto1.setTitle(afterTips.get(0).getTitle());
        tipResponseDto1.setContent(afterTips.get(0).getContent());
        tipResponseDto1.setCoverPhoto(afterTips.get(0).getCoverPhoto());
        tipResponseDto1.setMemberId(afterTips.get(0).getMember().getMemberId());
        tipResponseDto1.setNickname(afterTips.get(0).getMember().getNickname());

        TagDto tagDto1 = new TagDto();
        tagDto1.setTagId(tip1.getTags().get(0).getTagId());
        tagDto1.setTagContent(tip1.getTags().get(0).getTagContent());
        TagDto tagDto2 = new TagDto();
        tagDto2.setTagId(tip1.getTags().get(1).getTagId());
        tagDto2.setTagContent(tip1.getTags().get(1).getTagContent());
        List<TagDto> tagDtoList = new ArrayList<>();
        tagDtoList.add(tagDto1);
        tagDtoList.add(tagDto2);
        tipResponseDto1.setTags(tagDtoList);

        List<TipReplyResponseDto> tipReplyResponseDtoList = new ArrayList<>();

        TipReplyResponseDto tipReplyResponseDto = new TipReplyResponseDto();

        tipReplyResponseDto.setTipReplyId(afterTips.get(0).getReplies().get(0).getTipReplyId());
        tipReplyResponseDto.setContent(afterTips.get(0).getReplies().get(0).getContent());
        tipReplyResponseDto.setNickname(afterTips.get(0).getReplies().get(0).getMember().getNickname());
        tipReplyResponseDto.setMemberId(afterTips.get(0).getReplies().get(0).getMember().getMemberId());
        tipReplyResponseDto.setTipId(afterTips.get(0).getReplies().get(0).getTip().getTipId());
        tipReplyResponseDto.setCreatedDateTime(afterTips.get(0).getReplies().get(0).getCreatedDateTime());

        tipReplyResponseDtoList.add(tipReplyResponseDto);

        tipResponseDto1.setReplies(tipReplyResponseDtoList);

        TipResponseDto tipResponseDto2 = new TipResponseDto();
        tipResponseDto2.setTipId(afterTips.get(1).getTipId());
        tipResponseDto2.setCreatedDateTime(afterTips.get(1).getCreatedDateTime());
        tipResponseDto2.setTitle(afterTips.get(1).getTitle());
        tipResponseDto2.setContent(afterTips.get(1).getContent());
        tipResponseDto2.setCoverPhoto(afterTips.get(1).getCoverPhoto());
        tipResponseDto2.setMemberId(afterTips.get(1).getMember().getMemberId());
        tipResponseDto2.setNickname(afterTips.get(1).getMember().getNickname());

        TagDto tagDto3 = new TagDto();
        tagDto3.setTagId(tip2.getTags().get(0).getTagId());
        tagDto3.setTagContent(tip2.getTags().get(0).getTagContent());
        TagDto tagDto4 = new TagDto();
        tagDto4.setTagId(tip2.getTags().get(1).getTagId());
        tagDto4.setTagContent(tip2.getTags().get(1).getTagContent());
        List<TagDto> tagDtoList2 = new ArrayList<>();
        tagDtoList2.add(tagDto3);
        tagDtoList2.add(tagDto4);
        tipResponseDto2.setTags(tagDtoList2);

        List<TipReplyResponseDto> tipReplyResponseDtoList2 = new ArrayList<>();

        TipReplyResponseDto tipReplyResponseDto2 = new TipReplyResponseDto();

        tipReplyResponseDto2.setTipReplyId(afterTips.get(1).getReplies().get(0).getTipReplyId());
        tipReplyResponseDto2.setContent(afterTips.get(1).getReplies().get(0).getContent());
        tipReplyResponseDto2.setNickname(afterTips.get(1).getReplies().get(0).getMember().getNickname());
        tipReplyResponseDto2.setMemberId(afterTips.get(1).getReplies().get(0).getMember().getMemberId());
        tipReplyResponseDto2.setTipId(afterTips.get(1).getReplies().get(0).getTip().getTipId());
        tipReplyResponseDto2.setCreatedDateTime(afterTips.get(1).getReplies().get(0).getCreatedDateTime());

        tipReplyResponseDtoList2.add(tipReplyResponseDto2);

        tipResponseDto2.setReplies(tipReplyResponseDtoList2);

        List<TipResponseDto> tipResponseDtoList = new ArrayList<>();


//        given(mapper.tipToTipResponseDto(Mockito.any(Tip.class))).willReturn(tipResponseDto2);
//        given(mapper.tipToTipResponseDto(Mockito.any(Tip.class))).willReturn(tipResponseDto1);

        given(mapper.tipToTipResponseDto(Mockito.any(Tip.class))).willReturn(tipResponseDto2);

        tipResponseDtoList.add(tipResponseDto1);
        tipResponseDtoList.add(tipResponseDto2);

        String searchTag = "tag";
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", "1");

        ResultActions actions =
                mockMvc.perform(
                        get("/tip/searchTag/{search-tag}", searchTag).params(queryParams)
                                .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[1].tipId").value(tip2.getTipId()))
                .andExpect(jsonPath("$.data[1].title").value(tip2.getTitle()))
                .andExpect(jsonPath("$.data[1].memberId").value(tip2.getMember().getMemberId()))
                .andExpect(jsonPath("$.data[1].nickname").value(tip2.getMember().getNickname()))
                .andExpect(jsonPath("$.data[1].content").value(tip2.getContent()))
                .andExpect(jsonPath("$.data[1].tags[0].tagContent").value(tip2.getTags().get(0).getTagContent()))
                .andExpect(jsonPath("$.data[1].tags[1].tagContent").value(tip2.getTags().get(1).getTagContent()))
                .andExpect(jsonPath("$.data[1].replies[0].tipId").value(tip2.getReplies().get(0).getTip().getTipId()))
                .andExpect(jsonPath("$.data[1].replies[0].content").value(tip2.getReplies().get(0).getContent()));

    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void deleteTipTest() throws Exception {
        long tipId = 1L;

        doNothing().when(tipService).deleteTip(Mockito.anyLong());

        ResultActions actions = mockMvc.perform(delete("/tip/{tip-id}", tipId));

        actions.andExpect(status().isNoContent());
    }

}
