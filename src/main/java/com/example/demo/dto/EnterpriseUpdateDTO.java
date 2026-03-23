package com.example.demo.dto;

import com.example.demo.enums.SizeEnterprise;
import lombok.Getter;

@Getter
public class EnterpriseUpdateDTO {
    private String enterpriseName;
    private String description;
    private String contact;
    private String socialReason;
    private Long numberOfEmployees;
    private String locality;
    private String headLine;
    private String sector;
    private SizeEnterprise sizeEnterprise;
    private String socialLinks;
    private String siteUrl;
}
