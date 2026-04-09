package com.example.demo.dto;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record GetAllUsersResponse(
        Long id,
        String name,
        String email,
        Set<RoleResponse> roles,
        LocalDateTime createdAt
) {

    public static GetAllUsersResponse fromEntity(User user, String name) {
        return new GetAllUsersResponse(
                user.getId(),
                name,
                user.getEmail(),
                user.getRoles().stream()
                        .map(RoleResponse::fromEntity)
                        .collect(Collectors.toSet()),
                user.getCreatedAt()
        );
    }
}

