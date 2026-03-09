package com.example.demo.repositories;

import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("""
       SELECT DISTINCT u FROM User u
       LEFT JOIN FETCH u.roles r
       LEFT JOIN FETCH r.permissions
       WHERE u.email = :email
       """)
    Optional<User> findByEmailWithRoles(String email);
}
