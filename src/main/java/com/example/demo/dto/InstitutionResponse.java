package com.example.demo.dto;

import com.example.demo.entities.Institution;
import com.example.demo.repositories.InstitutionRepository;

public record InstitutionResponse(
    String name,
    String country,
    String domain
) {
    public static InstitutionResponse fromEntity(Institution institution) {
        return new InstitutionResponse (
                    institution.getName(),
                    institution.getCountry(),
                    institution.getDomain()
                );
    }
}
