package com.example.demo.repositories;

import com.example.demo.entities.PostImpression;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface PostImpressionRepository extends JpaRepository<PostImpression, Long> {

    List<PostImpression> findByUserIdAndExpiresAtAfter(Long userId, OffsetDateTime now);
}
