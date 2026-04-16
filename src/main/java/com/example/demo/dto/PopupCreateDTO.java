package com.example.demo.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@ToString
public class PopupCreateDTO {
    private String title;
    private String callToActionUrl;
}
