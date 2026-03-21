package com.peddit.peddit_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostRequest {

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 300, message = "Título deve ter no maximo 300 caracteres")
    private String title;

    @NotBlank(message = "Conteúdo é obrigatório")
    private String content;

    @NotNull(message = "Comunidade é obrigatória")
    private Long communityId;
}
