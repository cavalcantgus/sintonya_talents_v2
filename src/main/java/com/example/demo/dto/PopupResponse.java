package com.example.demo.dto;

import com.example.demo.entities.Popup;
import lombok.Getter;

public record PopupResponse(
        Long id,
        String title,
        String url
) {
    public static PopupResponse fromEntity(Popup popup) {
        return new PopupResponse(
                popup.getId(),
                popup.getTitle(),
                popup.getUrl()
        );
    }
}
