package com.example.demo.services;

import com.example.demo.dto.RoleResponse;
import com.example.demo.entities.Role;
import com.example.demo.enums.RoleName;
import com.example.demo.repositories.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleResponse> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(RoleResponse::fromEntity)
                .toList();
    };

    public RoleResponse findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada"));
        return RoleResponse.fromEntity(role);
    }

    public Role insert(String roleName) {

        RoleName enumRole = RoleName.valueOf(roleName.toUpperCase());

        return roleRepository
                .findByRoleName(enumRole)
                .orElseGet(() -> {
                    Role newRole = new Role(enumRole);
                    return roleRepository.save(newRole);
                });
    }
}
