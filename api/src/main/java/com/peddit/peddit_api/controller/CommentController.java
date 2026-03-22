package com.peddit.peddit_api.controller;

import com.peddit.peddit_api.dto.request.CommentRequest;
import com.peddit.peddit_api.dto.response.CommentResponse;
import com.peddit.peddit_api.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Endpoints de comentários")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Criar comentário em um post", security = @SecurityRequirement(name ="bearerAuth"))
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentRequest request, Authentication authentication) {
        CommentResponse response = commentService.createComment(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
