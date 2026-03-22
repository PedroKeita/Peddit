package com.peddit.peddit_api.dto.response;

import com.peddit.peddit_api.entity.Post;
import lombok.Builder;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostDetailResponse {

    private Long id;
    private String title;
    private String content;
    private AuthorResponse author;
    private CommunitySimpleResponse community;
    private Integer score;
    private List<CommentResponse> comments;
    private LocalDateTime createdAt;


    public static PostDetailResponse from(Post post, List<CommentResponse> comments) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(AuthorResponse.from(post.getAuthor()))
                .community(CommunitySimpleResponse.from(post.getCommunity()))
                .score(post.getScore())
                .comments(comments)
                .createdAt(post.getCreatedAt())
                .build();
    }


}
