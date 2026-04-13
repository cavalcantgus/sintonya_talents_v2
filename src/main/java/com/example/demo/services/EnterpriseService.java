package com.example.demo.services;

import com.example.demo.dto.CandidateResponse;
import com.example.demo.dto.EnterpriseUpdateDTO;
import com.example.demo.dto.EnterpriseResponse;
import com.example.demo.entities.Candidate;
import com.example.demo.entities.Enterprise;
import com.example.demo.entities.Profile;
import com.example.demo.entities.User;
import com.example.demo.repositories.EnterpriseRepository;
import com.example.demo.repositories.ProfileRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final SupabaseStorageService storageService;

    @Value("${app.upload.dir:/var/uploads}")
    private String uploadDir;

    public EnterpriseService(EnterpriseRepository enterpriseRepository,
                             ProfileRepository profileRepository,
                             UserRepository userRepository,
                             SupabaseStorageService storageService) {
        this.enterpriseRepository = enterpriseRepository;
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    public List<EnterpriseResponse> findAll() {
        return enterpriseRepository.findAll()
                .stream()
                .map(EnterpriseResponse::fromEntity)
                .toList();
    }

    public EnterpriseResponse findById(Long id) {
        Enterprise enterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        return EnterpriseResponse.fromEntity(enterprise);
    }

    public EnterpriseResponse findByUserId(Long id) {
        Enterprise enterprise = enterpriseRepository.findByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        return EnterpriseResponse.fromEntity(enterprise);
    }

    public EnterpriseResponse update(UserDetails userDetails, EnterpriseUpdateDTO objDto) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Enterprise enterprise = enterpriseRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        Profile profile = profileRepository.findById(enterprise.getProfile().getId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado"));


        enterprise.setEnterpriseName(objDto.getEnterpriseName());
        enterprise.setDescription(objDto.getDescription());
        enterprise.setContact(objDto.getContact());
        enterprise.setSocialReason(objDto.getSocialReason());
        enterprise.setNumberOfEmployees(objDto.getNumberOfEmployees());
        enterprise.setSector(objDto.getSector());
        enterprise.setSizeEnterprise(objDto.getSizeEnterprise());
        enterprise.setSiteUrl(objDto.getSiteUrl());
        enterprise.setSector(objDto.getSector());

        profile.setLocality(objDto.getLocality());
        profile.setSocialLinks(objDto.getSocialLinks());
        profile.setHeadLine(objDto.getHeadLine());
        enterpriseRepository.save(enterprise);
        profileRepository.save(profile);

        return EnterpriseResponse.fromEntity(enterprise);
    }

    @Transactional
    public EnterpriseResponse update(UserDetails userDetails, String personalSummary) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Enterprise enterprise = enterpriseRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        Profile profile = profileRepository.findById(enterprise.getProfile().getId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado"));

        profile.setPersonalSummary(personalSummary);
        profileRepository.save(profile);
        enterprise.setProfile(profile);
        enterpriseRepository.save(enterprise);
        return EnterpriseResponse.fromEntity(enterprise);
    }


    public void updateProfilePhoto(MultipartFile file, Long id) throws IOException {
        validateFile(file);

        Enterprise enterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        Profile profile = enterprise.getProfile();

        if(profile.getPhoto() != null) {
            String oldPath = extractPath(profile.getPhoto());
            storageService.delete(oldPath);
        }

        String path = "users/enterprise/" + id + "/photo_" + System.currentTimeMillis() + ".jpg";
        String publicUrl = storageService.upload(file, path);

        profile.setPhoto(publicUrl);
        profileRepository.save(profile);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }
    }

    private String extractPath(String publicUrl) {
        return publicUrl.replaceAll(".*/public/[^/]+/", "");
    }

    public void updateProfileBanner(MultipartFile file, Long id) throws IOException {
        validateFile(file);

        Enterprise enterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        Profile profile = enterprise.getProfile();

        if (profile.getBanner() != null) {
            String oldPath = extractPath(profile.getBanner());
            storageService.delete(oldPath);
        }

        String path = "users/enterprise/" + id + "/banner_" + System.currentTimeMillis() + ".jpg";
        String publicUrl = storageService.upload(file, path);

        profile.setBanner(publicUrl);
        profileRepository.save(profile);
    }
}
