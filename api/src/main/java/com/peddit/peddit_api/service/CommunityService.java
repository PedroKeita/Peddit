package com.peddit.peddit_api.service;

import com.peddit.peddit_api.dto.response.CommunityResponse;
import com.peddit.peddit_api.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;

    public Page<CommunityResponse> listCommunities(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return communityRepository.findAll(pageable).map(CommunityResponse::from);
    }
}
