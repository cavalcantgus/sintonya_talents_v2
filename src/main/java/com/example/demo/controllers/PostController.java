package com.example.demo.controllers;

import com.example.demo.dto.PostCreateRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.enums.PostType;
import com.example.demo.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
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

//    @PostMapping("/create-post")
//    public ResponseEntity<Void> createPost(
//            @Valid @RequestBody PostCreateRequest request,
//            @AuthenticationPrincipal UserDetails user
//    ) {
//        postService.create(request, user);
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        PostResponse post = postService.findById(id);
        return ResponseEntity.ok().body(post);
    }

    @PostMapping(value = "/create-post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createPost(
            @Valid @RequestPart("data") PostCreateRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetails user
    ) throws IOException {
        postService.create(request, user, file);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/publications-posts/by-author")
    public ResponseEntity<List<PostResponse>> findByPostTypeAndUsersId(@AuthenticationPrincipal UserDetails userDetails) {
        PostResponse posts = postService.findByPostTypeAndUsersId(PostType.PUBLICATION, userDetails)
    }

}
