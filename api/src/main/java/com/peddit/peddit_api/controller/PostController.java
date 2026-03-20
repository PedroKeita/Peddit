package com.peddit.peddit_api.controller;

import com.peddit.peddit_api.dto.response.PostResponse;
import com.peddit.peddit_api.service.PostService;
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
}
