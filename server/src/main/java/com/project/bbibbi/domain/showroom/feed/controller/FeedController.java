package com.project.bbibbi.domain.showroom.feed.controller;

import com.project.bbibbi.domain.showroom.feed.entity.Feed;
import com.project.bbibbi.domain.showroom.feed.mapper.FeedMapper;
import com.project.bbibbi.domain.showroom.feed.service.FeedService;
import com.project.bbibbi.domain.showroom.feed.dto.*;
import com.project.bbibbi.domain.showroom.feed.entity.*;
import com.project.bbibbi.domain.showroom.feed.dto.FeedPatchDto;
import com.project.bbibbi.domain.showroom.feed.dto.FeedPostDto;
import com.project.bbibbi.domain.showroom.feed.dto.FeedResponseDto;
import com.project.bbibbi.global.response.MultiResponseDto;
import com.project.bbibbi.global.response.PageAbleResponseDto;
import com.project.bbibbi.global.response.SingleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/feed")
@Validated
public class FeedController {

    private final static String FEED_DEFAULT_URL = "/feed";

    private final FeedService feedService;

    private final FeedMapper mapper;

    public FeedController(FeedService feedService, FeedMapper mapper) {
        this.feedService = feedService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postFeed(@Valid @RequestBody FeedPostDto requestBody) {

        Feed feed = mapper.feedPostDtoToFeed(requestBody);

        Feed createdFeed = feedService.createFeed(feed);

        URI location = UriComponentsBuilder.newInstance().path(
                FEED_DEFAULT_URL + "/{feed-id}").buildAndExpand(createdFeed.getFeedId()).toUri();

        FeedResponseDto feedResponseDto = mapper.feedToFeedResponseDto(createdFeed);

        return ResponseEntity.created(location).body(new SingleResponseDto<>(feedResponseDto));

    }

    @PatchMapping("/{feed-id}")
    public ResponseEntity patchFeed(@PathVariable("feed-id") @Positive long feedId,
                                    @Valid @RequestBody FeedPatchDto requestBody){

        requestBody.setFeedId(feedId);

        Feed feed = mapper.feedPatchDtoToFeed(requestBody);

        Feed updatedFeed = feedService.updateFeed(feed);

        FeedResponseDto feedResponseDto = mapper.feedToFeedResponseDto(updatedFeed);

        return new ResponseEntity<>(new SingleResponseDto<>(feedResponseDto), HttpStatus.OK);
    }

    @GetMapping("/{feed-id}")
    public ResponseEntity getFeed(@PathVariable("feed-id") @Positive long feedId){
        Feed feed = feedService.findFeed(feedId);

        FeedResponseDto feedResponseDto = mapper.feedToFeedResponseDto(feed);

        return new ResponseEntity<>(new SingleResponseDto<>(feedResponseDto), HttpStatus.OK);
    }

    @GetMapping("/filter/{search-code}")
    public ResponseEntity getFeeds(@PathVariable("search-code") String searchCode,
                                   @RequestParam int page) {

        // 사이즈는 12로 고정
        int size = 12;

        Page<Feed> pageFeeds = feedService.findFeeds(searchCode, page - 1, size);

        List<Feed> feeds = pageFeeds.getContent();

        List<FeedResponseDto> feedResponseDtos = mapper.feedsToFeedResponseDtos(feeds);

        PageAbleResponseDto pageAbleResponseDto = new PageAbleResponseDto();

        if(feeds.size() != 0) {
            if (feeds.get(0).getFinalPage()) {
                pageAbleResponseDto.setIsLast(true);
            } else {
                pageAbleResponseDto.setIsLast(false);
            }
        }

        pageAbleResponseDto.setData(feedResponseDtos);

        return new ResponseEntity<>(pageAbleResponseDto, HttpStatus.OK);

    }

    @GetMapping("/search/{search-string}")
    public ResponseEntity getSearchFeeds(@PathVariable("search-string") String searchString,
                                         @RequestParam int page) {

        // 사이즈는 12로 고정
        int size = 12;

        List<Feed> pageFeeds = feedService.findSearchFeeds(searchString, page - 1, size);

        List<FeedResponseDto> feedResponseDtos = mapper.feedsToFeedResponseDtos(pageFeeds);

        PageAbleResponseDto pageAbleResponseDto = new PageAbleResponseDto();

        if(pageFeeds.size() != 0) {
            if (pageFeeds.get(0).getFinalPage()) {
                pageAbleResponseDto.setIsLast(true);
            } else {
                pageAbleResponseDto.setIsLast(false);
            }
        }

        pageAbleResponseDto.setData(feedResponseDtos);

        return new ResponseEntity<>(pageAbleResponseDto, HttpStatus.OK);

    }

    @GetMapping("/likeTop10")
    public ResponseEntity getLikeTopTen(){

        List<Feed> pageFeeds = feedService.findLikeTopTen();

        List<FeedResponseDto> feedResponseDtos = mapper.feedsToFeedResponseDtos(pageFeeds);

        return new ResponseEntity<>(new MultiResponseDto<>(feedResponseDtos), HttpStatus.OK);

    }

    @DeleteMapping("/{feed-id}")
    public ResponseEntity deleteFeed(@PathVariable("feed-id") @Positive long feedId){
        feedService.deleteFeed(feedId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
