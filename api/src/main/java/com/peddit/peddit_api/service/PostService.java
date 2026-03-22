package com.peddit.peddit_api.service;

import com.peddit.peddit_api.dto.request.PostRequest;
import com.peddit.peddit_api.dto.response.CommentResponse;
import com.peddit.peddit_api.dto.response.PostDetailResponse;
import com.peddit.peddit_api.dto.response.PostResponse;
import com.peddit.peddit_api.entity.Community;
import com.peddit.peddit_api.entity.Post;
import com.peddit.peddit_api.entity.User;
import com.peddit.peddit_api.exception.ResourceNotFoundException;
import com.peddit.peddit_api.repository.CommentRepository;
import com.peddit.peddit_api.repository.PostRepository;
import com.peddit.peddit_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.peddit.peddit_api.repository.CommunityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;

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

    public PostResponse createPost(PostRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Community community = communityRepository.findById(request.getCommunityId())
                .orElseThrow(() -> new IllegalArgumentException("Comunidade não encontrada"));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(user)
                .community(community)
                .score(0)
                .build();

        return PostResponse.from(postRepository.save(post));
    }


    public PostDetailResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post não encontrado"));

        List<CommentResponse> comments = commentRepository
                .findByPostIdAndParentIsNullOrderByCreatedAtDesc(id)
                .stream()
                .map(CommentResponse::from)
                .toList();

        return PostDetailResponse.from(post, comments);
    }
}
