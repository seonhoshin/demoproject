package com.project.bbibbi.domain.goodTip.tip.dto;

import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import com.project.bbibbi.domain.goodTip.tipReply.dto.TipReplyResponseDto;
import com.project.bbibbi.domain.goodTip.tipTag.dto.TagDto;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Tip}
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipResponseDto {

    private Long tipId;

    @NotBlank
    private String title;

    @NotBlank
    private String coverPhoto;

    @NotBlank
    private String content;

    private int views;

    private Long memberId;
    private String nickname;
    private String memberImage;
    private String myIntro;

    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;

    private int likeCount;
    private Boolean likeYn;
    private int bookmarkCount;
    private Boolean bookmarkYn;
    private int repliesCount;
    private Boolean followYn;
    private Boolean fixYn;

    private List<TipReplyResponseDto> replies;

    private List<TagDto> tags;

}