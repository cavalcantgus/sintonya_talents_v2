package com.example.demo.services;

import com.example.demo.dto.*;
import com.example.demo.entities.*;
import com.example.demo.enums.PostType;
import com.example.demo.enums.RoleName;
import com.example.demo.enums.VacancyStatus;
import com.example.demo.repositories.FeedItemScoreRepository;
import com.example.demo.repositories.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final VacancyService vacancyService;
    private final UserService userService;
    private final PublicationService publicationService;
    private final FeedItemScoreRepository feedItemScoreRepository;

    public PostService(PostRepository postRepository, VacancyService vacancyService, UserService userService,
                       PublicationService publicationService,
                       FeedItemScoreRepository feedItemScoreRepository) {
        this.postRepository = postRepository;
        this.vacancyService = vacancyService;
        this.userService = userService;
        this.publicationService = publicationService;
        this.feedItemScoreRepository = feedItemScoreRepository;
    }

    public List<PostResponse> findAll() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::fromEntity)
                .toList();
    }

    @Transactional
    public PostResponse create(PostCreateRequest request, UserDetails user) {
        User author = userService.findByEmail(user.getUsername());
        boolean allowed = validateUserPermissions(author);

        if(!allowed) throw new RuntimeException("Você não tem permissão para acessar este recurso");

        Post post = new Post();
        post.setPostType(request.getType());
        post.getUsers().add(author);
        post.setVacancyStatus(VacancyStatus.PENDING_APPROVAL);
        author.getPosts().add(post);

        postRepository.save(post);

        if(request.getPostDataDTO() instanceof VacancyCreateDTO vacancyCreateDTO) {
            vacancyService.createVacany(vacancyCreateDTO, post);
        }

        if(request.getPostDataDTO() instanceof PublicationCreateDTO publicationCreateDTO) {
            publicationService.createPublication(publicationCreateDTO, post);
        }

        FeedItemScore feedItemScore = new FeedItemScore();

        feedItemScore.setScore(0.0);
        feedItemScore.setPost(post);

        post.setFeedItemScore(feedItemScore);

        feedItemScoreRepository.save(feedItemScore);

        return PostResponse.fromEntity(post);
    }

    private boolean validateUserPermissions(User author) {
        return author.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals(RoleName.ADMINISTRATOR)
                        || role.getRoleName().equals(RoleName.ENTERPRISE));

    }

}
