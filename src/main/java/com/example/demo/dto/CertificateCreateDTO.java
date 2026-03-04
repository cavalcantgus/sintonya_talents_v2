package com.example.demo.dto;

import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class CertificateCreateDTO {
    private String title;
    private String issuingOrganization;
    private String url;
    private Long hours;
    private String issueMonth;
    private String issueYear;
    private String expirationMonth;
    private String expirationYear;
    private Set<Long> skillsId;
}
