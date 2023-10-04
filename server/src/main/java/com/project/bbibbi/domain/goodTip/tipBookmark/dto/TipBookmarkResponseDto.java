package com.project.bbibbi.domain.goodTip.tipBookmark.dto;

import com.project.bbibbi.domain.goodTip.tipBookmark.entity.TipBookmark;
import lombok.*;

/**
 * DTO for {@link TipBookmark}
 */
@Getter
@Setter
@NoArgsConstructor
public class TipBookmarkResponseDto {
    private Long tipId;
    private Long memberId;
    private Boolean bookmarkYn;
    private Integer bookmarkCount;
}