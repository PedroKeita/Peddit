package com.peddit.peddit_api.controller;

import com.peddit.peddit_api.dto.request.PostRequest;
import com.peddit.peddit_api.dto.request.PostUpdateRequest;
import com.peddit.peddit_api.dto.response.PostDetailResponse;
import com.peddit.peddit_api.dto.response.PostResponse;
import com.peddit.peddit_api.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Endpoints de posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    @Operation(summary = "Listar posts com filtros e paginação")
    public ResponseEntity<Page<PostResponse>> listsPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sort,
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) String q
    ) {

        return ResponseEntity.ok(postService.listPosts(page, size, sort, communityId, q));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Visualizar post completo com comentários")
    public ResponseEntity<PostDetailResponse> getPostById(@PathVariable Long id) {

        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo post",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody PostRequest request,
            Authentication authentication) {
        PostResponse response = postService.createPost(request, authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar post (autor ou admin)",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(postService.updatePost(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar post (autor ou admin)",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deletePost(@PathVariable Long id, Authentication authentication) {

        postService.deletePost(id, authentication.getName());

        return ResponseEntity.noContent().build();
    }




}
