package com.peddit.peddit_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {

    @NotBlank(message = "Conteúdo é obrigatório")
    private String content;

    @NotNull(message = "Post é obrigatório")
    private Long postId;

    private Long parentId;
}
