package com.example.demo.repositories;

import com.example.demo.entities.Post;
import com.example.demo.enums.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
    SELECT p FROM Post p
    LEFT JOIN p.vacancy v
    LEFT JOIN p.feedItemScore f
    LEFT JOIN p.publication pub
    WHERE (
        (p.postType = 'PUBLICATION' AND pub IS NOT NULL)
        OR (
            p.postType = 'VACANCY'
            AND p.status = 'PENDING_APPROVAL'
            AND v IS NOT NULL
            AND p.closedAt IS NULL
            AND (v.expirationDate IS NULL OR v.expirationDate >= CURRENT_DATE)
        )
    )
    ORDER BY COALESCE(f.score, 0.0) DESC, p.createdAt DESC
""")
    List<Post> findAllActive();

    List<Post> findByPostTypeAndUsersId(PostType postType, Long userId);
}
