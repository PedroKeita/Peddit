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

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private CommentResponse mapToResponse(Comment comment) {

        List<CommentResponse> replies = comment.getReplies().stream()
                .map(this::mapToResponse)
                .toList();

        return CommentResponse.from(comment, replies);
    }

    @Transactional
    public CommentResponse createComment(CommentRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post não encontrado"));


        Comment parent = null;
        if (request.getParentId() != null && request.getParentId() > 0) {
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

        Comment saved = commentRepository.save(comment);
        return mapToResponse(saved);

    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPost(Long postId) {

        List<Comment> comments = commentRepository
                .findTopLevelCommentsWithAuthor(postId);

        return comments.stream()
                .map(this::mapToResponse)
                .toList();
    }
}