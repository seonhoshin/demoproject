package com.project.bbibbi.domain.goodTip.tipBookMark.dto;

import com.project.bbibbi.domain.goodTip.tipBookMark.entity.TipBookMark;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link TipBookMark}
 */
@Getter
@Setter
@NoArgsConstructor
public class TipBookMarkRequestDto {
    private Long tipId;
    private Long memberId;
}
