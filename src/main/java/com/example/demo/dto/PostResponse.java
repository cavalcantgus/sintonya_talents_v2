package com.example.demo.dto;

import com.example.demo.entities.Enterprise;
import com.example.demo.entities.Post;
import com.example.demo.enums.PostType;
import com.example.demo.enums.VacancyStatus;

public record PostResponse (
        Long id,
        PostType type,
        UserResponse author,
        VacancyStatus vacancyStatus,
        VacancyResponse vacancyResponse

){
    public static PostResponse fromEntity(Post post) {
        return new PostResponse(
                post.getId(),
                post.getPostType(),
                UserResponse.fromEntity(post.getUser()),
                post.getVacancyStatus(),
                VacancyResponse.fromEntity(post.getVacancy())
        );
    }
}
