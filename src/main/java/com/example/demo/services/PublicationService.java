package com.example.demo.services;

import com.example.demo.dto.PublicationCreateDTO;
import com.example.demo.entities.Post;
import com.example.demo.entities.Publication;
import com.example.demo.repositories.PublicationRepository;
import org.springframework.stereotype.Service;

@Service
public class PublicationService {

    private final PublicationRepository publicationRepository;

    public PublicationService(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    public Publication createPublication(PublicationCreateDTO objDto, Post post) {
        Publication publication = new Publication();
        publication.setTitle(objDto.getTitle());
        publication.setBody(objDto.getBody());
        publication.setPost(post);
        post.setPublication(publication);

        return publicationRepository.save(publication);
    }
}
