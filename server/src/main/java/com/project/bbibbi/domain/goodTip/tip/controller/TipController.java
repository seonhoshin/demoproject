package com.project.bbibbi.domain.goodTip.tip.controller;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.goodTip.tip.dto.TipPatchDto;
import com.project.bbibbi.domain.goodTip.tip.dto.TipPostDto;
import com.project.bbibbi.domain.goodTip.tip.dto.TipResponseDto;
import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import com.project.bbibbi.domain.goodTip.tip.mapper.TipMapper;
import com.project.bbibbi.domain.goodTip.tip.service.TipService;
//import com.project.bbibbi.domain.tipImage.service.TipImageService;
//import com.project.bbibbi.domain.tipTag.entity.TipTag;
//import com.project.bbibbi.domain.tipTag.repository.TipTagRepository;
//import com.project.bbibbi.domain.tipTag.service.TagService;
//import com.project.bbibbi.domain.tipTag.service.TipTagService;
import com.project.bbibbi.global.exception.tipexception.TipNotFoundException;
import com.project.bbibbi.global.response.MultiResponseDto;
import com.project.bbibbi.global.response.PageAbleResponseDto;
import com.project.bbibbi.global.response.SingleResponseDto;
import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Slice;
//import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/tip")
public class TipController {

    private final static String TIP_DEFAULT_URL = "/tip";

    private final TipService tipService;

    private final TipMapper tipMapper;

    public TipController(TipService tipService, TipMapper tipMapper) {
        this.tipService = tipService;
        this.tipMapper = tipMapper;

    }

    @PostMapping
    public ResponseEntity createTip(@RequestBody @Valid TipPostDto tipPostDto) {

        Tip tip = tipMapper.tipPostDtoToTip(tipPostDto);

        Tip createdTip = tipService.createTip(tip);

        TipResponseDto tipResponseDto = tipMapper.tipToTipResponseDto(createdTip);

        return ResponseEntity.created(URI.create("/tip/" + createdTip.getTipId())).body(new SingleResponseDto<>(tipResponseDto));
    }

    @PatchMapping("/{tip-id}")
    public ResponseEntity updateTip(
            @PathVariable("tip-id") Long tipId, @RequestBody @Valid TipPatchDto tipPatchDto) {

        tipPatchDto.setTipId(tipId);

        Tip tip = tipMapper.tipPatchDtoToTip(tipPatchDto);
        Tip updatedTip = tipService.updateTip(tipId, tip);

        if (updatedTip == null) {
            throw new TipNotFoundException();
        }

        TipResponseDto tipResponseDto = tipMapper.tipToTipResponseDto(updatedTip);

        return new ResponseEntity<>(new SingleResponseDto<>(tipResponseDto), HttpStatus.OK);
    }
    @GetMapping("/{tip-id}")
    public ResponseEntity getTip(@PathVariable("tip-id") Long tipId) {
        Tip tip = tipService.getTip(tipId);

        if (tip == null) {
            throw new TipNotFoundException();
        }

        TipResponseDto tipResponseDto = tipMapper.tipToTipResponseDto(tip);

        return new ResponseEntity<>(new SingleResponseDto<>(tipResponseDto), HttpStatus.OK);
    }

    @DeleteMapping("/{tip-id}")
    public ResponseEntity<Void> deleteTip(@PathVariable("tip-id") Long tipId) {
        tipService.deleteTip(tipId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myInfoSearch")
    public ResponseEntity getMyInfoTips(@RequestParam int page) {

        Long myInfoMemberId = loginUtils.getLoginId();

        // 사이즈는 4로 고정
        int size = 4;

        List<Tip> pageTips = tipService.findMyInfoTips(myInfoMemberId);

        List<TipResponseDto> tipResponseDtos = pageTips.stream()
                .map(tipMapper::tipToTipResponseDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new MultiResponseDto<>(tipResponseDtos), HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity getAllTips(@RequestParam int page) {

        // 사이즈는 12로 고정
        int size = 12;

        Page<Tip> pageTips = tipService.getAllTips(page - 1, size);

        List<Tip> tips = pageTips.getContent();

        List<TipResponseDto> tipResponseDtos = tips.stream()
                .map(tipMapper::tipToTipResponseDto)
                .collect(Collectors.toList());

        PageAbleResponseDto pageAbleResponseDto = new PageAbleResponseDto<>();

        if(tips.size() != 0) {

            if (tips.get(0).getFinalPage()) {
                pageAbleResponseDto.setIsLast(true);

            } else {

                pageAbleResponseDto.setIsLast(false);
            }
        }

        pageAbleResponseDto.setData(tipResponseDtos);

        return ResponseEntity.ok(pageAbleResponseDto);

    }


    @GetMapping("/search/{search-string}")
    public ResponseEntity getAllSearchTips(@PathVariable("search-string") String searchString, @RequestParam int page) {

        // 사이즈는 12로 고정
        int size = 12;

        List<Tip> pageTips = tipService.getAllSearchTips(searchString, page - 1, size);

        List<TipResponseDto> tipResponseDtos = pageTips.stream()
                .map(tipMapper::tipToTipResponseDto)
                .collect(Collectors.toList());

        PageAbleResponseDto pageAbleResponseDto = new PageAbleResponseDto<>();

        if(pageTips.size() != 0) {
            if (pageTips.get(0).getFinalPage()) {
                pageAbleResponseDto.setIsLast(true);
            } else {
                pageAbleResponseDto.setIsLast(false);
            }
        }

        pageAbleResponseDto.setData(tipResponseDtos);

        return ResponseEntity.ok(pageAbleResponseDto);
    }

    @GetMapping("/searchTag/{search-tag}")
    public ResponseEntity getAllSearchTipTags(@PathVariable("search-tag") String searchTag, @RequestParam int page) {

        List<Tip> pageTips = tipService.getAllSearchTipTags(searchTag);

        List<TipResponseDto> tipResponseDtos = pageTips.stream()
                .map(tipMapper::tipToTipResponseDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new MultiResponseDto<>(tipResponseDtos), HttpStatus.OK);
    }

}
