package com.example.expertListing.controller;

import com.example.expertListing.dto.*;
import com.example.expertListing.security.UserPrincipal;
import com.example.expertListing.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<PostResponse>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginatedResponse<PostResponse> data = postService.getAllPosts(page, size);
        return ResponseEntity.ok(ApiResponse.success("Posts retrieved successfully", data));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody PostRequest request) {
        PostResponse data = postService.createPost(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Post created successfully", data));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Void>> toggleLike(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        postService.toggleLike(currentUser.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Like toggled successfully", null));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(@PathVariable Long id) {
        List<CommentResponse> data = postService.getCommentsForPost(id);
        return ResponseEntity.ok(ApiResponse.success("Comments retrieved successfully", data));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody CommentRequest request) {
        CommentResponse data = postService.addComment(currentUser.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("Comment added successfully", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        postService.deletePost(currentUser.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Post deleted successfully", null));
    }
}
