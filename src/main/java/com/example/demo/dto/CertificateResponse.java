package com.example.demo.dto;

import com.example.demo.entities.Candidate;
import com.example.demo.entities.Certificates;
import com.example.demo.entities.Institution;

import java.time.LocalDate;
import java.util.List;

public record CertificateResponse(
        Long id,
        String title,
        InstitutionResponse institutionResponse,
        String url,
        Long hours,
        LocalDate issueDate
) {
    public static CertificateResponse fromEntity(Certificates certificate) {
        return new CertificateResponse(
                certificate.getId(),
                certificate.getTitle(),
                InstitutionResponse.fromEntity(certificate.getInstitution()),
                certificate.getUrl(),
                certificate.getHours(),
                certificate.getIssueDate()
        );
    }
}
