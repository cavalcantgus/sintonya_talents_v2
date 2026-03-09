package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CertificateCreateDTO {
    private String title;
    private String issuingOrganization;
    private String url;
    private Long hours;
    private String issueMonth;
    private String issueYear;
    private String expirationMonth;
    private String expirationYear;
    private List<Long> skillsId;
    private MultipartFile file;
}
