package com.example.demo.services;

import com.example.demo.dto.CandidateResponse;
import com.example.demo.dto.CandidateUpdateDTO;
import com.example.demo.dto.SkillCandidateCreateDto;
import com.example.demo.entities.*;
import com.example.demo.enums.SkillLevel;
import com.example.demo.enums.SkillSource;
import com.example.demo.repositories.CandidateRepository;
import com.example.demo.repositories.ProfileRepository;
import com.example.demo.repositories.SkillBaseRepository;
import com.example.demo.repositories.SkillCandidateRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ProfileRepository profileRepository;
    private final SkillBaseRepository skillBaseRepository;
    private final SkillCandidateRepository skillCandidateRepository;

    public CandidateService(CandidateRepository candidateRepository,
                            ProfileRepository profileRepository,
                            SkillBaseRepository skillBaseRepository,
                            SkillCandidateRepository skillCandidateRepository) {
        this.candidateRepository = candidateRepository;
        this.profileRepository = profileRepository;
        this.skillBaseRepository = skillBaseRepository;
        this.skillCandidateRepository = skillCandidateRepository;
    }

    public List<CandidateResponse> findAll() {
        return candidateRepository.findAll()
                .stream()
                .map(CandidateResponse::fromEntity)
                .toList();
    }

    public Candidate findByUserEmail(String email) {
        return candidateRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));
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

    @Transactional
    public CandidateResponse addSkills(Long id, List<SkillCandidateCreateDto> objDto) {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

        Map<Long, String> skillLevelMap = objDto.stream()
                .collect(Collectors.toMap(
                        SkillCandidateCreateDto::getSkillId,
                        SkillCandidateCreateDto::getLevel
                ));

        Set<SkillBase> skills = new HashSet<>(
                skillBaseRepository.findAllById(skillLevelMap.keySet())
        );

        Set<Long> existingSkillsIds = skillCandidateRepository.findSkillIdsByCandidateIdAndSource(
                id, SkillSource.MANUAL);

        for (SkillBase skill : skills) {
            if (!existingSkillsIds.contains(skill.getId())) {

                SkillCandidate skillCandidate = new SkillCandidate();

                skillCandidate.setCandidate(candidate);
                skillCandidate.setSkillBase(skill);
                skillCandidate.setSource(SkillSource.MANUAL);

                String levelStr = skillLevelMap.get(skill.getId());
                skillCandidate.setSkillLevel(SkillLevel.valueOf(levelStr));

                candidate.getCandidateSkills().add(skillCandidate);
            }
        }

        candidateRepository.save(candidate);
        return CandidateResponse.fromEntity(candidate);
    }
}
