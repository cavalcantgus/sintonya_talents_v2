package com.example.demo.dto;

import com.example.demo.entities.Enterprise;
import com.example.demo.entities.Post;
import com.example.demo.enums.PostType;
import com.example.demo.enums.VacancyStatus;

import java.util.List;

public record PostResponse (
        Long id,
        PostType type,
        List<UserResponse> authors,
        VacancyStatus vacancyStatus,
        VacancyResponse vacancyResponse,
        PublicationResponse publicationResponse

){
    public static PostResponse fromEntity(Post post) {
        return new PostResponse(
                post.getId(),
                post.getPostType(),
                post.getUsers().stream()
                        .map(UserResponse::fromEntity)
                        .toList(),
                post.getVacancyStatus(),
                post.getVacancy() != null ? VacancyResponse.fromEntity(post.getVacancy()) : null,
                post.getPublication() != null ? PublicationResponse.fromEntity(post.getPublication()) : null
        );
    }
}
