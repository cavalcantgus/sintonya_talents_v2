package com.example.demo.services;

import com.example.demo.dto.*;
import com.example.demo.entities.*;
import com.example.demo.enums.AttachmentType;
import com.example.demo.enums.PostType;
import java.io.File;
import com.example.demo.enums.RoleName;
import com.example.demo.enums.VacancyStatus;
import com.example.demo.repositories.FeedItemScoreRepository;
import com.example.demo.repositories.FileRepository;
import com.example.demo.repositories.PostFileRepository;
import com.example.demo.repositories.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    @Value("${app.upload.dir:/var/uploads}")
    private String uploadDir;

    private final PostRepository postRepository;
    private final VacancyService vacancyService;
    private final UserService userService;
    private final PublicationService publicationService;
    private final FeedItemScoreRepository feedItemScoreRepository;
    private final FileRepository fileRepository;
    private final PostFileRepository postFileRepository;

    public PostService(PostRepository postRepository, VacancyService vacancyService, UserService userService,
                       PublicationService publicationService,
                       FeedItemScoreRepository feedItemScoreRepository,
                       FileRepository fileRepository,
                       PostFileRepository postFileRepository) {
        this.postRepository = postRepository;
        this.vacancyService = vacancyService;
        this.userService = userService;
        this.publicationService = publicationService;
        this.feedItemScoreRepository = feedItemScoreRepository;
        this.fileRepository = fileRepository;
        this.postFileRepository = postFileRepository;
    }

    public List<PostResponse> findAll() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::fromEntity)
                .toList();
    }

    public List<PostResponse> findByPostTypeAndUsersId(PostType postType, UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());

        return postRepository.findByPostTypeAndUsersId(postType, user.getId())
                .stream()
                .map(PostResponse::fromEntity)
                .toList();
    }

    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post não encontrado"));
        return PostResponse.fromEntity(post);
    }

    @Transactional
    public PostResponse create(PostCreateRequest request, UserDetails user, MultipartFile objFile) throws IOException {
        User author = userService.findByEmail(user.getUsername());

        if (!validateUserPermissions(author))
            throw new RuntimeException("Você não tem permissão para acessar este recurso");

        Post post = buildPost(request, author);
        postRepository.save(post);

        if (objFile != null && !objFile.isEmpty()) {
            attachFileToPost(objFile, post);
        }

        handlePostData(request, post);
        attachFeedScore(post);

        return PostResponse.fromEntity(post);
    }

    private Post buildPost(PostCreateRequest request, User author) {
        Post post = new Post();
        post.setPostType(request.getType());
        post.setVacancyStatus(VacancyStatus.PENDING_APPROVAL);
        post.getUsers().add(author);
        author.getPosts().add(post);
        return post;
    }

    private void attachFileToPost(MultipartFile objFile, Post post) throws IOException {
        String fileName = UUID.randomUUID() + "_" + objFile.getOriginalFilename();

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        objFile.transferTo(filePath.toFile());

        com.example.demo.entities.File file = new com.example.demo.entities.File();
        file.setFileName(objFile.getOriginalFilename());
        file.setContentType(objFile.getContentType());
        file.setSize(objFile.getSize());
        file.setPath(filePath.toString());

        fileRepository.save(file);

        PostFile postFile = new PostFile();
        postFile.setFile(file);
        postFile.setPost(post);
        postFileRepository.save(postFile);

        post.getPostFiles().add(postFile);
    }

    private void handlePostData(PostCreateRequest request, Post post) {
        if (request.getPostDataDTO() instanceof VacancyCreateDTO vacancyCreateDTO) {
            vacancyService.createVacany(vacancyCreateDTO, post);
        }
        if (request.getPostDataDTO() instanceof PublicationCreateDTO publicationCreateDTO) {
            publicationService.createPublication(publicationCreateDTO, post);
        }
    }

    private void attachFeedScore(Post post) {
        FeedItemScore feedItemScore = new FeedItemScore();
        feedItemScore.setScore(0.0);
        feedItemScore.setPost(post);
        post.setFeedItemScore(feedItemScore);
        feedItemScoreRepository.save(feedItemScore);
    }

    private boolean validateUserPermissions(User author) {
        return author.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals(RoleName.ADMINISTRATOR)
                        || role.getRoleName().equals(RoleName.ENTERPRISE));
    }

}
