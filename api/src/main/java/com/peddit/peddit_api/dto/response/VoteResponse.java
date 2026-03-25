package com.peddit.peddit_api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoteResponse {
    private Long postId;
    private Long commentId;
    private Integer score;
    private Integer userVote;
}
