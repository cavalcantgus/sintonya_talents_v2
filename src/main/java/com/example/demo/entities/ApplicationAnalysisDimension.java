package com.example.demo.entities;

import com.example.demo.enums.AnalysisDimension;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.mapping.Join;

@Entity
@Table(name = "application_analysis_dimension")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationAnalysisDimension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "analysis_id")
    private ApplicationAnalysis analysis;

    @Enumerated(EnumType.STRING)
    private AnalysisDimension dimension;

    private Double score;
    private Double weight;
    private Double weightedScore;
}
