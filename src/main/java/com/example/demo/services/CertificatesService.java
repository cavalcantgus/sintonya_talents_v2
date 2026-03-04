package com.example.demo.services;

import com.example.demo.dto.CandidateResponse;
import com.example.demo.dto.CertificateCreateDTO;
import com.example.demo.dto.CertificateResponse;
import com.example.demo.dto.VacancyResponse;
import com.example.demo.entities.*;
import com.example.demo.enums.SkillLevel;
import com.example.demo.repositories.CandidateRepository;
import com.example.demo.repositories.CertificatesRepository;
import com.example.demo.repositories.SkillBaseRepository;
import com.example.demo.repositories.SkillCandidateRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.engine.spi.EntityUniqueKey;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CertificatesService {

    private final CertificatesRepository certificatesRepository;
    private final CandidateRepository candidateRepository;
    private final SkillBaseRepository skillBaseRepository;
    private final SkillCandidateRepository skillCandidateRepository;

    public CertificatesService(CertificatesRepository certificatesRepository,
                               CandidateRepository candidateRepository,
                               SkillBaseRepository skillBaseRepository,
                               SkillCandidateRepository skillCandidateRepository) {
        this.certificatesRepository = certificatesRepository;
        this.candidateRepository = candidateRepository;
        this.skillBaseRepository = skillBaseRepository;
        this.skillCandidateRepository = skillCandidateRepository;
    }

    public List<CertificateResponse> findAll() {
        return certificatesRepository.findAll()
                .stream()
                .map(CertificateResponse::fromEntity)
                .toList();
    }

    public CertificateResponse findById(Long id) {
        Certificates certificate = certificatesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado"));

        return CertificateResponse.fromEntity(certificate);
    }

    @Transactional
    public CertificateResponse insert(Long candidateId, CertificateCreateDTO objDto) {

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

        Certificates certificate = new Certificates();
        certificate.setTitle(objDto.getTitle());
        certificate.setIssuingOrganization(objDto.getIssuingOrganization());
        certificate.setUrl(objDto.getUrl());
        certificate.setHours(objDto.getHours());
        certificate.setCandidate(candidate);

        candidate.getCertificates().add(certificate);

        Set<SkillBase> skills = new HashSet<>(
                skillBaseRepository.findAllById(objDto.getSkillsId())
        );

        Set<Long> existingSkillIds = candidate.getCandidateSkills()
                .stream()
                .map(sc -> sc.getSkillBase().getId())
                .collect(Collectors.toSet());

        for (SkillBase skill : skills) {

            if (!existingSkillIds.contains(skill.getId())) {
                SkillCandidate skillCandidate = new SkillCandidate();
                skillCandidate.setCandidate(candidate);
                skillCandidate.setSkillBase(skill);
                skillCandidate.setSkillLevel(SkillLevel.NON_SPECIFIED);

                candidate.getCandidateSkills().add(skillCandidate);
            }

            SkillCertificates skillCertificates = new SkillCertificates();
            skillCertificates.setCertificate(certificate);
            skillCertificates.setSkill(skill);

            certificate.getCertificateSkills().add(skillCertificates);
        }

        candidateRepository.save(candidate);

        return CertificateResponse.fromEntity(certificate);
    }
}
