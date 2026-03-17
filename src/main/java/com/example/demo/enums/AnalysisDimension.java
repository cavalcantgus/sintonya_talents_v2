package com.example.demo.enums;

public enum AnalysisDimension {
    REQUIRED_SKILL(0.10),
    OPTIONAL_SKILL(0.05),
    TECHNICAL(0.20),
    BEHAVIORAL(0.45),
    CERTIFICATES(0.10),
    EXPERIENCES(0.10);

    public final double weight;

    AnalysisDimension(Double weight) {
        this.weight = weight;
    }
}
