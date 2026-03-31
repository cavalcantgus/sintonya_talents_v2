package com.example.demo.dto;

import lombok.Getter;

@Getter
public class PublicationCreateDTO extends PostDataDTO{
    private String title;
    private String body;
}
