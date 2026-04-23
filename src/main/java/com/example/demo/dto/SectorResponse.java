package com.example.demo.dto;

import com.example.demo.entities.Sector;

public record SectorResponse(
        Long id,
        String title
) {
    public static SectorResponse fromEntity(Sector sector) {
        return new SectorResponse(
                sector.getId(),
                sector.getTitle()
        );
    }
}
