package com.example.demo.dto;

import com.example.demo.entities.Profile;

public record ProfileResponse(
        String photo,
        String bannerm,
        String headline,
        String personalSummary,
        String socialLinks,
        String locality
) {

    public static ProfileResponse fromEntity(Profile profile) {
        return new ProfileResponse(
                profile.getPhoto(),
                profile.getBanner(),
                profile.getHeadLine(),
                profile.getPersonalSummary(),
                profile.getSocialLinks(),
                profile.getLocality()
        );
    }
}
