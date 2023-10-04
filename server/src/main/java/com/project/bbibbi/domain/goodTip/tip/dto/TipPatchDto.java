package com.project.bbibbi.domain.goodTip.tip.dto;

import com.project.bbibbi.domain.goodTip.tip.entity.Tip;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * DTO for {@link Tip}
 */
@Data
@Getter
@Setter
@NoArgsConstructor
public class TipPatchDto {
    private Long tipId;

    @NotBlank
    private String title;

    private String coverPhoto;

    @NotBlank
    private String content;

    private Long memberId;

    private List<String> tagContents;

    public List<String> tagContents() {
        return tagContents;
    }


}