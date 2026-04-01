package com.example.demo.controllers;

import com.example.demo.dto.PostResponse;
import com.example.demo.services.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> feed() {
        List<PostResponse> posts = feedService.buildFeed();
        return ResponseEntity.ok().body(posts);
    }
}
