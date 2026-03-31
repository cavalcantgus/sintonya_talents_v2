package com.example.demo.dto;

import com.example.demo.entities.Publication;

public record PublicationResponse(
        Long id,
        String title,
        String body
) {
    public static PublicationResponse fromEntity(Publication publication) {
        return new PublicationResponse(
                publication.getId(),
                publication.getTitle(),
                publication.getBody()
        );
    }
}
