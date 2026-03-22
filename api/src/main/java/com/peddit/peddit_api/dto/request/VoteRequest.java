package com.peddit.peddit_api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequest {

    @NotNull(message = "Value é obrigatório")
    @Min(value = -1, message = "Value deve ser -1 ou 1")
    @Max(value = 1, message = "Value deve ser -1 ou 1")
    private Integer value;
}
