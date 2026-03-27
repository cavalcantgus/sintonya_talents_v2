package com.example.demo.services;

import com.example.demo.dto.*;
import com.example.demo.entities.Enterprise;
import com.example.demo.entities.Post;
import com.example.demo.entities.User;
import com.example.demo.entities.Vacancy;
import com.example.demo.enums.PostType;
import com.example.demo.enums.RoleName;
import com.example.demo.enums.VacancyStatus;
import com.example.demo.repositories.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final VacancyService vacancyService;
    private final UserService userService;
    private final EnterpriseService enterpriseService;

    public PostService(PostRepository postRepository, VacancyService vacancyService, UserService userService,
                       EnterpriseService enterpriseService) {
        this.postRepository = postRepository;
        this.vacancyService = vacancyService;
        this.userService = userService;
        this.enterpriseService = enterpriseService;
    }

    public PostResponse insertPostByEnterprise(Long userId, PostCreateVacancyDTO objDto) {
        User user = userService.findById(userId);

        boolean isEnterprise = user.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals(RoleName.ENTERPRISE));

        if (!isEnterprise) {
            throw new RuntimeException("Usuário não tem permissão para criar vagas");
        }

        Post post = new Post();
        post.setPostType(objDto.getType());
        post.setUser(user);
        postRepository.save(post);

        if(objDto.getType().equals(PostType.VACANCY)) {
            insertPostVacancy( objDto.getVacancyCreateDTO(), post, userId);
        }

        if(objDto.getType().equals(PostType.PUBLICATION)) {
//            insertPostVacancy( objDto.getVacancyCreateDTO(), post, userId);
        }

        return PostResponse.fromEntity(post);
    }

    private void insertPostVacancy(VacancyCreateDTO objDto, Post post, Long userId) {
        EnterpriseResponse enterprise = enterpriseService.findByUserId(userId);

        Vacancy vacancy = vacancyService.createVacany(enterprise.id(), objDto, post);
        post.setVacancyStatus(VacancyStatus.APPROVED);
        post.setVacancy(vacancy);
    }

    public PostResponse insertPostByAdministrator(Long userId, PostCreateVacancyDTO objDto) {
        User user = userService.findById(userId);

        boolean isEnterprise = user.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals(RoleName.ADMINISTRATOR));

        if (!isEnterprise) {
            throw new RuntimeException("Usuário não tem permissão para criar vagas");
        }

        Post post = new Post();
        post.setPostType(objDto.getType());
        post.setUser(user);
        postRepository.save(post);

//        if(objDto.getType().equals(PostType.VACANCY)) {
////            insertPostVacancy( objDto.getVacancyCreateDTO(), post, userId);
//        }

//        if(objDto.getType().equals(PostType.PUBLICATION)) {
//            insertPostPublication();
//        }

        return PostResponse.fromEntity(post);
    }

//    private void insertPostPublication(PublicationCreateDTO objDto) {
//        EnterpriseResponse enterprise = enterpriseService.findByUserId(userId);
//
//        Vacancy vacancy = vacancyService.createVacany(enterprise.id(), objDto, post);
//        post.setVacancyStatus(VacancyStatus.APPROVED);
//        post.setVacancy(vacancy);
//    }
}
