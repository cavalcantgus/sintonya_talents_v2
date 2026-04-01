package com.example.demo.services;

import com.example.demo.dto.PostResponse;
import com.example.demo.entities.Post;
import com.example.demo.enums.PostType;
import com.example.demo.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedService {

    private final PostRepository postRepository;

    public FeedService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostResponse> buildFeed() {

        // Separação dos dois Pools
        List<Post> vacancies = postRepository.findAll()
                .stream()
                .filter(post -> post.getPostType() != PostType.PUBLICATION)
                .toList();

        List<Post> publications = postRepository.findAll()
                .stream()
                .filter(post -> post.getPostType() != PostType.VACANCY)
                .toList();

        System.out.println("TAMANHO DO PUBLICATIONS: " + publications.size());
        System.out.println("TAMANHO DO VACANCIES: " + vacancies.size());

        return injectVacancies(publications, vacancies, 0.15, 5);
//        return postRepository.findAllActive()
//                .stream()
//                .map(PostResponse::fromEntity)
//                .toList();
    }

    private List<PostResponse> injectVacancies(List<Post> publications, List<Post> vacancies, double lambda, int maxVacancies) {
        List<Post> feed = new ArrayList<>();
        int pubIndex = 0;
        int vacIndex = 0;
        int vacCount = 0;
        int gaps = 999; // alto pra primeira vaga poder aparecer cedo

        while (pubIndex < publications.size()) {
            double p = 1 - Math.exp(-lambda * gaps);
            boolean shouldInject = Math.random() < p
                    && vacIndex < vacancies.size()
                    && vacCount < maxVacancies;

            if (shouldInject) {
                feed.add(vacancies.get(vacIndex++));
                vacCount++;
                gaps = 0;
            } else {
                feed.add(publications.get(pubIndex++));
                gaps++;
            }
        }

        return feed.stream()
                .map(PostResponse::fromEntity)
                .toList();
    }
}
