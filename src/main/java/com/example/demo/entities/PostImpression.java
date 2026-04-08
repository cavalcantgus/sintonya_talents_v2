package com.example.demo.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "post_impression")
public class PostImpression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @CreationTimestamp
    @Column(name = "seen_at", nullable = false, updatable = false)
    private OffsetDateTime seenAt;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    private boolean interacted;
}
