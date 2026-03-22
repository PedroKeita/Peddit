package com.peddit.peddit_api.service;

import com.peddit.peddit_api.dto.request.CommentRequest;
import com.peddit.peddit_api.dto.response.CommentResponse;
import com.peddit.peddit_api.entity.Comment;
import com.peddit.peddit_api.entity.Post;
import com.peddit.peddit_api.entity.User;
import com.peddit.peddit_api.exception.ResourceNotFoundException;
import com.peddit.peddit_api.repository.CommentRepository;
import com.peddit.peddit_api.repository.PostRepository;
import com.peddit.peddit_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse createComment(CommentRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post não encontrado"));


        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Comentário pai não encontrado"));


            if (!parent.getPost().getId().equals(post.getId())) {
                throw new IllegalArgumentException("Comentário pai não pertence a este post");
            }
        }

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(user)
                .post(post)
                .parent(parent)
                .score(0)
                .build();

        return CommentResponse.from(commentRepository.save(comment));

    }
}