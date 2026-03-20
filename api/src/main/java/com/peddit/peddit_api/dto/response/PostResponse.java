package com.peddit.peddit_api.dto.response;

import com.peddit.peddit_api.entity.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponse {

    private Long id;
    private String title;
    private String contentPreview;
    private AuthorResponse author;
    private CommunitySimpleResponse community;
    private Integer score;
    private long commentCount;
    private LocalDateTime createdAt;

    public static PostResponse from(Post post) {
        String content = post.getContent();
        String preview = content.length() > 200
                ? content.substring(0,200) + "..."
                : content;

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contentPreview(preview)
                .author(AuthorResponse.from(post.getAuthor()))
                .community(CommunitySimpleResponse.from(post.getCommunity()))
                .score(post.getScore())
                .commentCount(0L)
                .createdAt(post.getCreatedAt())
                .build();
    }
}
