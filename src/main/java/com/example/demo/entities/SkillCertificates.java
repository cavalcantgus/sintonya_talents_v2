package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "skill_certificate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkillCertificates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "skill_base_id", nullable = false)
    private SkillBase skill;

    @ManyToOne
    @JoinColumn(name = "certiticate_id", nullable = false)
    private Certificates certificate;
}
