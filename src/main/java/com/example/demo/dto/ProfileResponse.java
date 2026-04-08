package com.example.demo.dto;

import com.example.demo.entities.Profile;

public record ProfileResponse(
        String photo,
        String headline,
        String personalSummary,
        String socialLinks,
        String locality,
        String banner
) {

    public static ProfileResponse fromEntity(Profile profile) {
        if (profile == null) return null;

        return new ProfileResponse(
                profile.getPhoto(),
                profile.getHeadLine(),
                profile.getPersonalSummary(),
                profile.getSocialLinks(),
                profile.getLocality(),
                profile.getBanner()
        );
    }
}
