package com.peddit.peddit_api.dto.response;

import com.peddit.peddit_api.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorResponse {

    private Long id;
    private String username;

    public static AuthorResponse from(User user) {
        return AuthorResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
