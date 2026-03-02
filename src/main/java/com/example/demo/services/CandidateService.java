package com.example.demo.services;

import com.example.demo.dto.CandidateResponse;
import com.example.demo.dto.CandidateUpdateDTO;
import com.example.demo.entities.Candidate;
import com.example.demo.entities.Profile;
import com.example.demo.repositories.CandidateRepository;
import com.example.demo.repositories.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ProfileRepository profileRepository;

    public CandidateService(CandidateRepository candidateRepository,
                            ProfileRepository profileRepository) {
        this.candidateRepository = candidateRepository;
        this.profileRepository = profileRepository;
    }

    public List<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    public CandidateResponse findById(Long id) {
       Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

       return CandidateResponse.fromEntity(candidate);
    }

    public CandidateResponse findByUserId(Long id) {
       Candidate candidate = candidateRepository.findByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

        return CandidateResponse.fromEntity(candidate);
    }

    public CandidateResponse update(Long id, CandidateUpdateDTO objDto) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

        candidate.setFullName(objDto.getFullName());
        candidate.setGender(objDto.getGender());
        candidate.setRaceEthnicity(objDto.getRaceEthnicity());
        candidate.setSector(objDto.getSector());
        candidate.setContact(objDto.getContact());

        Profile profile = profileRepository.findById(candidate.getProfile().getId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado"));

        profile.setLocality(objDto.getLocality());
        profile.setSocialLinks(objDto.getSocialLinks());
        profile.setHeadLine(objDto.getHeadLine());
        candidateRepository.save(candidate);
        profileRepository.save(profile);

        return CandidateResponse.fromEntity(candidate);
    }

    @Transactional
    public CandidateResponse update(Long id, String personalSummary) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

        Profile profile = profileRepository.findById(candidate.getProfile().getId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado"));

        profile.setPersonalSummary(personalSummary);
        profileRepository.save(profile);
        candidate.setProfile(profile);
        candidateRepository.save(candidate);
        return CandidateResponse.fromEntity(candidate);
    }
}
