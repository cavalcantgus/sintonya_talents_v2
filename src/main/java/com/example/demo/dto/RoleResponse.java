package com.example.demo.dto;

import com.example.demo.entities.Role;

public record RoleResponse(
        Long id,
        String name,
        String description
) {
    public static RoleResponse fromEntity(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getName(),
                role.getDescription()
        );
    }
}
