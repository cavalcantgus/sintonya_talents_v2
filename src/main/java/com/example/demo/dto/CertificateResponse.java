package com.example.demo.dto;

import com.example.demo.entities.Candidate;
import com.example.demo.entities.Certificates;

import java.util.List;

public record CertificateResponse(
        Long id,
        String title,
        String issuingOrganization,
        String url,
        Long hours
) {
    public static CertificateResponse fromEntity(Certificates certificate) {
        return new CertificateResponse(
                certificate.getId(),
                certificate.getTitle(),
                certificate.getIssuingOrganization(),
                certificate.getUrl(),
                certificate.getHours()

        );
    }
}
