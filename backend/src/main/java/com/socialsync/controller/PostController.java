package com.socialsync.controller;

import com.socialsync.dto.CreatePostRequest;
import com.socialsync.dto.PostResponse;
import com.socialsync.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(postService.getAllPosts(userId));
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
        PostResponse response = postService.createAndPublishPost(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PostMapping("/deliveries/{deliveryId}/retry")
    public ResponseEntity<PostResponse> retryDelivery(@PathVariable Long deliveryId) {
        PostResponse response = postService.retryDelivery(deliveryId);
        return ResponseEntity.ok(response);
    }
}
