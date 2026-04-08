package com.example.demo.controllers;

import com.example.demo.dto.PostResponse;
import com.example.demo.services.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<List<PostResponse>> feed(@AuthenticationPrincipal UserDetails user) {
        List<PostResponse> posts = feedService.buildFeed(user);
        return ResponseEntity.ok().body(posts);
    }
}
