package com.project.bbibbi.domain.goodTip.tipTag.dto;

import com.project.bbibbi.domain.goodTip.tipTag.entity.Tag;
import lombok.*;

/**
 * DTO for {@link Tag}
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDto  {
    private Long tagId;
    private String tagContent;
}