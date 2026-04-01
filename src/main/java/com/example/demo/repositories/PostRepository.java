package com.example.demo.repositories;

import com.example.demo.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
    SELECT p FROM Post p
    WHERE (
        (p.postType = 'PUBLICATION' AND p.publication IS NOT NULL)
        OR (
            p.postType = 'VACANCY'
            AND p.vacancyStatus = 'PENDING_APPROVAL'
            AND p.vacancy IS NOT NULL
            AND p.vacancy.vacancyStatus = 'PENDING_APPROVAL'
            AND p.vacancy.closedAt IS NULL
            AND (p.vacancy.expirationDate IS NULL OR p.vacancy.expirationDate >= CURRENT_DATE)
        )
    )
    ORDER BY COALESCE(p.feedItemScore.score, 0.0) DESC, p.createdAt DESC
""")
    List<Post> findAllActive();
}
