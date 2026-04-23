package com.example.demo.utils;

import java.util.List;

public class DomainExtractor {
    public static String extract(String website) {
        if (website == null || website.isBlank()) return null;
        return website
                .replaceAll("https?://(www\\.)?", "")
                .replaceAll("/.*", "")
                .trim();
    }
}