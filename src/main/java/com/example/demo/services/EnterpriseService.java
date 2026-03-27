package com.example.demo.services;

import com.example.demo.dto.CandidateResponse;
import com.example.demo.dto.EnterpriseUpdateDTO;
import com.example.demo.dto.EnterpriseResponse;
import com.example.demo.entities.Candidate;
import com.example.demo.entities.Enterprise;
import com.example.demo.entities.Profile;
import com.example.demo.repositories.EnterpriseRepository;
import com.example.demo.repositories.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Value("${app.upload.dir:/var/uploads}")
    private String uploadDir;

    public EnterpriseService(EnterpriseRepository enterpriseRepository,
                             ProfileRepository profileRepository) {
        this.enterpriseRepository = enterpriseRepository;
        this.profileRepository = profileRepository;
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

    public EnterpriseResponse update(Long id, EnterpriseUpdateDTO objDto) {
        Enterprise enterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        enterprise.setEnterpriseName(objDto.getEnterpriseName());
        enterprise.setDescription(objDto.getDescription());
        enterprise.setContact(objDto.getContact());
        enterprise.setSocialReason(objDto.getSocialReason());
        enterprise.setNumberOfEmployees(objDto.getNumberOfEmployees());
        enterprise.setSector(objDto.getSector());
        enterprise.setSizeEnterprise(objDto.getSizeEnterprise());
        enterprise.setSiteUrl(objDto.getSiteUrl());
        enterprise.setSector(objDto.getSector());

        Profile profile = profileRepository.findById(enterprise.getProfile().getId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado"));

        profile.setLocality(objDto.getLocality());
        profile.setSocialLinks(objDto.getSocialLinks());
        profile.setHeadLine(objDto.getHeadLine());
        enterpriseRepository.save(enterprise);
        profileRepository.save(profile);

        return EnterpriseResponse.fromEntity(enterprise);
    }

//    @Transactional
//    public CandidateResponse update(Long id, String personalSummary) {
//        Candidate candidate = candidateRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));
//
//        Profile profile = profileRepository.findById(candidate.getProfile().getId())
//                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado"));
//
//        profile.setPersonalSummary(personalSummary);
//        profileRepository.save(profile);
//        candidate.setProfile(profile);
//        candidateRepository.save(candidate);
//        return CandidateResponse.fromEntity(candidate);
//    }


    public void updateProfilePhoto(MultipartFile file, Long id) throws IOException {
        System.out.println(">>> Iniciando upload para empresa id: " + id);
        System.out.println(">>> Arquivo recebido: " + file.getOriginalFilename() + " | Tamanho: " + file.getSize());

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }

        Enterprise enterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
        System.out.println(">>> Empresa encontrada: " + enterprise.getId());

        Path dirPath = Paths.get(uploadDir, "enterprises", String.valueOf(enterprise.getId()));
        System.out.println(">>> Diretório alvo (absoluto): " + dirPath.toAbsolutePath());
        Files.createDirectories(dirPath);
        System.out.println(">>> Diretório criado/confirmado");

        Profile profile = enterprise.getProfile();
        System.out.println(">>> Profile atual: " + profile);

//        if (profile == null) {
//            profile = new Profile();
//            profile.setEnterprise(enterprise);
//            System.out.println(">>> Novo profile criado");
//        } else if (profile.getPhoto() != null) {
//            Files.deleteIfExists(Paths.get(profile.getPhoto()));
//        }

        String fileName = "photo_" + System.currentTimeMillis() + ".jpg";
        Path photoPath = dirPath.resolve(fileName);
        Files.write(photoPath, file.getBytes());
        System.out.println(">>> Arquivo salvo em: " + photoPath.toAbsolutePath());

        profile.setPhoto(photoPath.toString());
        profileRepository.save(profile);
        System.out.println(">>> Profile salvo no banco com foto: " + profile.getPhoto());
    }
}
