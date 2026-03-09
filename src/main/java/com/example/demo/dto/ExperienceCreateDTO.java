package com.example.demo.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;

@Getter
public class ExperienceCreateDTO {

    private String title;
    private String workType;
    private String organization;
    private LocalDate startDate;
    private LocalDate endDate;
    private String locality;
    private boolean isCurrent;
    private String workModality;
    private String description;
    private Set<Long> skillsId;
}
