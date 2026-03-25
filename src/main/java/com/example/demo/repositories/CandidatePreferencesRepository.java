package com.example.demo.repositories;

import com.example.demo.entities.CandidatePreferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatePreferencesRepository extends JpaRepository<CandidatePreferences, Long> {

}
