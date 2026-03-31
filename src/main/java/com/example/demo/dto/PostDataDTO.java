package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = VacancyCreateDTO.class, name = "VACANCY"),
        @JsonSubTypes.Type(value = PublicationCreateDTO.class, name = "PUBLICATION")
})
public abstract class PostDataDTO {
}
