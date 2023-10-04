package com.project.bbibbi.domain.goodTip.tipBookmark.dto;

import com.project.bbibbi.domain.goodTip.tipBookmark.entity.TipBookmark;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link TipBookmark}
 */
@Getter
@Setter
@NoArgsConstructor
public class TipBookmarkRequestDto {
    private Long tipId;
    private Long memberId;
}
