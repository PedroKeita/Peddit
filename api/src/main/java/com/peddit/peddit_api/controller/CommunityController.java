package com.peddit.peddit_api.controller;

import com.peddit.peddit_api.dto.request.CommunityRequest;
import com.peddit.peddit_api.dto.response.CommunityResponse;
import com.peddit.peddit_api.service.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @Operation(summary = "Criar nova comunidade (Admin)",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CommunityResponse> createCommunity(
            @Valid @RequestBody CommunityRequest request,
            Authentication authentication) {

        CommunityResponse response = communityService
                .createCommunity(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
