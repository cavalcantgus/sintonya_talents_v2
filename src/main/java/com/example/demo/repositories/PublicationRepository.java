package com.example.demo.repositories;

import com.example.demo.entities.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationRepository extends JpaRepository<Publication, Long> {

}
