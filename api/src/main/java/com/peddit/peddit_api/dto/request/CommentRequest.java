package com.peddit.peddit_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {

    @NotBlank(message = "Conteúdo é obrigatório")
    private String content;

    @NotNull(message = "Post é obrigatório")
    private Long postId;

    @Schema(description = "ID do comentário pai (opcional)")
    private Long parentId;
}
