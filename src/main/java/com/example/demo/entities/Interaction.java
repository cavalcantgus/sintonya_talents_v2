package com.example.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "interaction")
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_impression_id")
    private PostImpression postImpression;


}
