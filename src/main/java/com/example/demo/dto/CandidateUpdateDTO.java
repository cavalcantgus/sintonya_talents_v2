package com.example.demo.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CandidateUpdateDTO {
    private String fullName;
    private String gender;
    private String raceEthnicity;
    private String headLine;
    private Long sector;
    private String locality;
    private String socialLinks;
    private String contact;
}

