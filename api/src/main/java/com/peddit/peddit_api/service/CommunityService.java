package com.peddit.peddit_api.service;

import com.peddit.peddit_api.dto.request.CommunityRequest;
import com.peddit.peddit_api.dto.response.CommunityResponse;
import com.peddit.peddit_api.entity.Community;
import com.peddit.peddit_api.entity.User;
import com.peddit.peddit_api.entity.UserRole;
import com.peddit.peddit_api.repository.CommunityRepository;
import com.peddit.peddit_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;

    public Page<CommunityResponse> listCommunities(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return communityRepository.findAll(pageable).map(CommunityResponse::from);
    }

    public CommunityResponse createCommunity(CommunityRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (user.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Acesso negado");
        }

        if (communityRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Já existe uma comunidade com esse nome");
        }

        Community community = Community.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdBy(user)
                .build();

        return CommunityResponse.from(communityRepository.save(community));
    }
}
