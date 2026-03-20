package com.peddit.peddit_api.service;

import com.peddit.peddit_api.dto.response.PostResponse;
import com.peddit.peddit_api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Page<PostResponse> listPosts(int page, int size, String sort,
                                        Long communityId, String q) {

        Sort sorting = sort != null && sort.equals("score")
                ? Sort.by("score").descending()
                : Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page, size, sorting);

        return postRepository.findAllWithFilters(
                communityId,
                q != null && !q.isBlank() ? q : null,
                pageable
        ).map(PostResponse::from);
    }
}
