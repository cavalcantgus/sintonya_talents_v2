package com.example.demo.services;

import com.example.demo.dto.PostResponse;
import com.example.demo.entities.Post;
import com.example.demo.entities.Subscription;
import com.example.demo.entities.User;
import com.example.demo.enums.Plan;
import com.example.demo.enums.PostType;
import com.example.demo.enums.SubscriptionStatus;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public FeedService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<PostResponse> buildFeed(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        // Separação dos dois Pools
        List<Post> vacancies = postRepository.findAll()
                .stream()
                .filter(post -> post.getPostType() != PostType.PUBLICATION)
                .toList();

        List<Post> publications = postRepository.findAll()
                .stream()
                .filter(post -> post.getPostType() != PostType.VACANCY)
                .toList();

        Subscription subscription = user.getSubscription();
        boolean isPremium = subscription.getPlan() == Plan.PREMIUM
                && subscription.getStatus() == SubscriptionStatus.ACTIVE;

        double lambda = isPremium ? 0.5 : user.getUserFeedConfig().getVacancyLambda();
        int poolSize = isPremium ? Integer.MAX_VALUE : user.getUserFeedConfig().getPoolSize();

        List<PostResponse> posts = injectVacancies(publications, vacancies, lambda, poolSize);
        long maxVacancies = posts.stream()
                .filter(p -> p.type().equals(PostType.VACANCY))
                .count();

        long maxPublications = posts.stream()
                .filter(p -> p.type().equals(PostType.PUBLICATION))
                .count();

        System.out.println("TAMANHO DO PUBLICATIONS: " + maxPublications);
        System.out.println("TAMANHO DO VACANCIES: " + maxVacancies);

        return posts;
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
