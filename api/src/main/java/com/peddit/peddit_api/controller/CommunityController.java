package com.peddit.peddit_api.controller;

import com.peddit.peddit_api.dto.response.CommunityResponse;
import com.peddit.peddit_api.service.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
@Tag(name = "Comunidades", description = "Endpoints de comunidades")
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping
    @Operation(summary = "Listar todas as comunidades")
    public ResponseEntity<Page<CommunityResponse>> listCommunities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(communityService.listCommunities(page, size));
    }
}
