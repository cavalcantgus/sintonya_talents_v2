package com.example.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "publication")
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private Post post;
}
