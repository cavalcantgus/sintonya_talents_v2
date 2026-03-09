package com.example.demo.entities;

import com.example.demo.enums.StageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "selection_stage")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SelectionStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "vacancy_id", nullable = false)
    private Vacancy vacancy;

    @Enumerated(EnumType.STRING)
    private StageType stageType;

    private String url;
    private Integer stageOrder;
}
