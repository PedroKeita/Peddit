package com.peddit.peddit_api.dto.response;

import com.peddit.peddit_api.entity.Community;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommunitySimpleResponse {

    private Long id;
    private String name;

    public static CommunitySimpleResponse from(Community community) {
        return CommunitySimpleResponse.builder()
                .id(community.getId())
                .name(community.getName())
                .build();
    }
}
