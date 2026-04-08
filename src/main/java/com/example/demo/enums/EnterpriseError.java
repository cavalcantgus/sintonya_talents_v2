package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnterpriseError {
    NOT_AUTHORIZED_TO_PUBLISH_VACANCY("Esta empresa não está autorizada a publicar vagas");

    private final String message;
}
