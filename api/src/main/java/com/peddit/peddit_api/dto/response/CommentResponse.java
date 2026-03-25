package com.peddit.peddit_api.dto.response;

import com.peddit.peddit_api.entity.Comment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentResponse {

    private Long id;
    private String content;
    private AuthorResponse author;
    private Integer score;
    private List<CommentResponse> replies;
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment comment, List<CommentResponse> replies) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(AuthorResponse.from(comment.getAuthor()))
                .score(comment.getScore())
                .replies(replies)
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
