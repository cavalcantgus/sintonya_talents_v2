package com.example.demo.repositories;

import com.example.demo.entities.Certificates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificatesRepository extends JpaRepository<Certificates, Long> {
}
