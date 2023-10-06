package com.project.bbibbi.domain.goodTip.tipBookMark.dto;

import com.project.bbibbi.domain.goodTip.tipBookMark.entity.TipBookMark;
import lombok.*;

/**
 * DTO for {@link TipBookMark}
 */
@Getter
@Setter
@NoArgsConstructor
public class TipBookMarkResponseDto {
    private Long tipId;
    private Long memberId;
    private Boolean bookmarkYn;
    private Integer bookmarkCount;
}