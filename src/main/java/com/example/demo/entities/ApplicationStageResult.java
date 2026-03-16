package com.example.demo.entities;

import com.example.demo.enums.StageStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "application_stage_result")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApplicationStageResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_application_id")
    private JobApplication jobApplication;

    @ManyToOne
    @JoinColumn(name = "selection_stage_id")
    private SelectionStage selectionStage;

    @Enumerated(EnumType.STRING)
    private StageStatus stageStatus;

    private Double score;
    private boolean locked = true;
}
