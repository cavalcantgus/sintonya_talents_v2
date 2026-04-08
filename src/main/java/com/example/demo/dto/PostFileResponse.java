package com.example.demo.dto;

import com.example.demo.entities.PostFile;

public record PostFileResponse(
        String url
) {
    public static PostFileResponse fromEntity(PostFile postFile) {
        return new PostFileResponse(
                postFile.getFile().getPath()
        );
    }
}
