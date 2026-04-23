package com.example.demo.repositories;

import com.example.demo.entities.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    List<Institution> findByNameContainingIgnoreCase(String name);
}
