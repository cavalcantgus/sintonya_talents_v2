package com.example.demo.dto;

import com.example.demo.entities.Candidate;
import com.example.demo.entities.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record CandidateResponse(
        Long id,
        String fullName,
        String gender,
        String raceEthnicity,
        LocalDate dateOfBirth,
        String cpf,
        String contact,
        ProfileResponse profileResponse,
        List<SkillCandidateResponse> skills,
        List<CertificateResponse> certificates
) {
    public static CandidateResponse fromEntity(Candidate candidate) {
        return new CandidateResponse(
                candidate.getId(),
                candidate.getFullName(),
                candidate.getGender(),
                candidate.getRaceEthnicity(),
                candidate.getDateOfBirth(),
                candidate.getCpf(),
                candidate.getContact(),
                ProfileResponse.fromEntity(candidate.getProfile()),

                candidate.getCandidateSkills()
                        .stream()
                        .map(SkillCandidateResponse::fromEntity)
                        .toList(),

                candidate.getCertificates()
                        .stream()
                        .map(CertificateResponse::fromEntity)
                        .toList()
        );
    }
}
