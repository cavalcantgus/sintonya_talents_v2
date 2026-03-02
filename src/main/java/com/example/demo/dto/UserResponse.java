package com.example.demo.dto;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public record UserResponse(
        Long id,
        String email
) {

    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail()
        );
    }
}
