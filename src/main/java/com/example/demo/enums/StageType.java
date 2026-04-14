package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StageType {

    TECHNICAL_TEST(true, 2),
    BEHAVIORAL_TEST(true, 1),

    SKILL_REQUIRED(false, null),
    SKILL_OPTIONAL(false, null),
    CERTIFICATE(false, null),
    EXPERIENCE(false, null);

    private final boolean visible;
    private final Integer defaultOrder;
}