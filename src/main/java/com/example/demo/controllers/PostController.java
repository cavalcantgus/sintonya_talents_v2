package com.example.demo.controllers;

import com.example.demo.dto.PostCreateVacancyDTO;
import com.example.demo.dto.PostResponse;
import com.example.demo.entities.Post;
import com.example.demo.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }



    @PostMapping("/create-vacancy/{userId}")
    public ResponseEntity<PostResponse> createVacancy(@RequestBody PostCreateVacancyDTO objDto, @PathVariable Long userId) {
        PostResponse postResponse = postService.insertPostByEnterprise(userId, objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(postResponse.id()).toUri();
        return ResponseEntity.created(uri).body(postResponse);
    }

}
