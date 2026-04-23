package com.example.demo.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class EducationUpdateDTO {
    private Long educationalInstitutionId;
    private String diploma;
    private String description;
    private Long studyArea;
    List<Long> educationSkills;
    private String startMonth;
    private String startYear;
    private String endMonth;
    private String endYear;
}
