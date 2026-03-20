package com.peddit.peddit_api.dto.response;

import com.peddit.peddit_api.entity.Community;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommunityResponse {

    private Long id;
    private String name;
    private String description;
    private long postCount;
    private LocalDateTime createdAt;

    public static CommunityResponse from(Community community) {
        return CommunityResponse.builder()
                .id(community.getId())
                .name(community.getName())
                .description(community.getDescription())
                .postCount(community.getPosts().size())
                .createdAt(community.getCreatedAt())
                .build();
    }
}
