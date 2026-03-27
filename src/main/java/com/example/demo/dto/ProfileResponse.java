package com.example.demo.dto;

import com.example.demo.entities.Profile;

public record ProfileResponse(
        String photo,
        String headline,
        String personalSummary,
        String socialLinks,
        String locality
) {

    public static ProfileResponse fromEntity(Profile profile) {
        if (profile == null) return null;

        String photo = profile.getPhoto() != null
                ? "/uploads/" + profile.getPhoto().replace("/var/uploads/", "")
                : null;

        return new ProfileResponse(
                photo,
                profile.getHeadLine(),
                profile.getLocality(),
                profile.getPersonalSummary(),
                profile.getSocialLinks()
        );
    }
}
