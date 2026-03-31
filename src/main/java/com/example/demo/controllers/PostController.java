package com.example.demo.controllers;

import com.example.demo.dto.PostCreateRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAll() {
        List<PostResponse> posts = postService.findAll();
        return ResponseEntity.ok().body(posts);
    }

    @PostMapping("/create-post")
    public ResponseEntity<Void> createPost(
            @Valid @RequestBody PostCreateRequest request,
            @AuthenticationPrincipal UserDetails user
    ) {
        postService.create(request, user);
        return ResponseEntity.ok().build();
    }
}
