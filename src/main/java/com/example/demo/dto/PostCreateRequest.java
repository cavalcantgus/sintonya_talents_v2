package com.example.demo.dto;

import com.example.demo.enums.PostType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateRequest {

    @NotNull
    private PostType type;

    @Valid
    @NotNull
    private PostDataDTO postDataDTO;
}
