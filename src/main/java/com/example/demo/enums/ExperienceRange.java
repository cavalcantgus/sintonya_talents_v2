package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Lombok;

@Getter
@AllArgsConstructor
public enum ExperienceRange {

    ZERO_TO_SIX_MONTHS(0L, 6L),
    ONE_TO_TWO_YEARS(12L, 24L),
    TWO_TO_FOUR_YEARS(24L, 48L),
    FOUR_PLUS_YEARS(48L, Long.MAX_VALUE);

    private final Long minMonths;
    private final Long maxMonths;
}