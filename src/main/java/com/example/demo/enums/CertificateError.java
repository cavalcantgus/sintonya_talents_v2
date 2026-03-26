package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum CertificateError {
    INVALID_PASSWORD("Senha inválida ou arquivo incorreto"),
    EXPIRED("Certificado expirado"),
    NOT_YET_VALID("Certificado ainda não é válido"),
    INVALID_CNPJ("CNPJ do certificado não confere"),
    INVALID_FILE("Erro ao ler certificado"),
    INVALID_SOCIAL_REASON("Razão Social incorreta"),
    INVALID_FANTASY_NAME("Nome fantasia não corresponde"),
    INACTIVE_CNJP("O CNPJ em questão está inativo");

    private final String message;
}
