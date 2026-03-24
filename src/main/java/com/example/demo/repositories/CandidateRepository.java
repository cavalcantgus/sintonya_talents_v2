package com.example.demo.repositories;

import com.example.demo.entities.Candidate;
import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    Optional<Candidate> findByUserId(Long id);
    Optional<Candidate> findByCpf(String cpf);

    @Query("""
    SELECT c FROM Candidate c
    WHERE c.user.email = :email
""")
    Optional<Candidate> findByUserEmail(String email);
}
