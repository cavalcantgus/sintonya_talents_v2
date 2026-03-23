package com.example.demo.dto;

import com.example.demo.entities.Enterprise;

public record EnterpriseResponse(
        Long id,
        String enterpriseName,
        String description,
        String cnpj,
        String contact,
        String socialReason,
        String sizeEnterprise,
        Long numberOfEmployees,
        String sector,
        String siteUrl,
        ProfileResponse profileResponse,
        boolean isApproved
) {
    public static EnterpriseResponse fromEntity(Enterprise enterprise) {
        return new EnterpriseResponse(
                enterprise.getId(),
                enterprise.getEnterpriseName(),
                enterprise.getDescription(),
                enterprise.getCnpj(),
                enterprise.getContact(),
                enterprise.getSocialReason(),
                enterprise.getSizeEnterprise() != null ? enterprise.getSizeEnterprise().toString() : null,
                enterprise.getNumberOfEmployees(),
                enterprise.getSector(),
                enterprise.getSiteUrl(),
                ProfileResponse.fromEntity(
                        enterprise.getProfile()
                ),
                enterprise.isApproved()
        );
    }
}
