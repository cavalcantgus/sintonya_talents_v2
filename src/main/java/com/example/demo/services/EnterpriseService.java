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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;
    private final ProfileRepository profileRepository;

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

}
