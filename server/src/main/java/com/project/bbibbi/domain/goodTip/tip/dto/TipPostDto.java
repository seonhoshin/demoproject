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
@AllArgsConstructor
@NoArgsConstructor
public class TipPostDto {

    @NotBlank
    private String title;

    private String coverPhoto;

    @NotBlank
    private String content;

    private Long memberId;

    public List<String> tagContents;

    public List<String> tagContents() {
        return tagContents;
    }


}