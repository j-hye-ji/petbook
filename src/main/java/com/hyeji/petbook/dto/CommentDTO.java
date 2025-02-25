package com.hyeji.petbook.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {

    private Long id;
    private Long postId;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
