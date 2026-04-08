package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessError {

    INVALID_CNPJ("Não foi possível consultar o CNPJ"),
    INATIVE_CNPJ("CNPJ não está ativo"),
    INVALID_SOCIAL_REASON("Razão Social incorreta"),
    INVALID_FANTASY_NAME("Nome fantasia não corresponde");

    private final String message;
}
