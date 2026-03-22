package com.peddit.peddit_api.service;

import com.peddit.peddit_api.dto.request.VoteRequest;
import com.peddit.peddit_api.dto.response.VoteResponse;
import com.peddit.peddit_api.entity.Post;
import com.peddit.peddit_api.entity.User;
import com.peddit.peddit_api.entity.Vote;
import com.peddit.peddit_api.exception.ResourceNotFoundException;
import com.peddit.peddit_api.repository.PostRepository;
import com.peddit.peddit_api.repository.UserRepository;
import com.peddit.peddit_api.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public VoteResponse vote(Long postId, VoteRequest request, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post não encontrado"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (post.getAuthor().getId().equals(user.getId())) {
            throw new AccessDeniedException("Você não pode votar no próprio post");
        }

        Optional<Vote> existing = voteRepository.findByUserIdAndPostId(user.getId(), postId);

        Integer userVote;

        if(existing.isPresent()) {
            Vote vote = existing.get();
            if (vote.getValue().equals(request.getValue())) {
                // Se for mesmo voto, vai cancelar
                post.setScore(post.getScore() - vote.getValue());
                voteRepository.delete(vote);
                userVote = null;
            } else {
                // Se voto for o oposto, vai trocar
                post.setScore(post.getScore() - vote.getValue() + request.getValue());
                vote.setValue(request.getValue());
                voteRepository.save(vote);
                userVote = request.getValue();
            }
        } else {
            // NOVO VOTO
            Vote vote = Vote.builder()
                    .user(user)
                    .post(post)
                    .value(request.getValue())
                    .build();
            voteRepository.save(vote);
            post.setScore(post.getScore() + request.getValue());
            userVote = request.getValue();
        }

        postRepository.save(post);

        return VoteResponse.builder()
                .postId(postId)
                .score(post.getScore())
                .userVote(userVote)
                .build();

    }
}
