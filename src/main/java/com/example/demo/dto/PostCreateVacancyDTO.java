package com.example.demo.dto;

import com.example.demo.enums.PostType;
import lombok.Getter;

@Getter
public class PostCreateVacancyDTO {
    private PostType type;
    private VacancyCreateDTO vacancyCreateDTO;
}
