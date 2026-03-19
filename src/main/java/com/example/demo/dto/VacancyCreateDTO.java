package com.example.demo.dto;

import com.example.demo.enums.VacancyType;
import com.example.demo.enums.WorkModality;
import lombok.Getter;

import java.util.List;

@Getter
public class VacancyCreateDTO {

    private String title;
    private String description;
    private String position;
    private String vacancyType;
    private Long sectorId;
    private boolean hasATechnicalTest;
    private boolean hasABehavioralTest;
    private boolean requiredCertificate;
    private String experienceRange;
    private String locality;
    private String modalityType;
    private List<SkillVacancyDTO> skills;
}
