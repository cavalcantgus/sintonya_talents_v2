package com.example.demo.dto;

import lombok.Getter;

@Getter
public class CnpjApiResponse {
    private String cnpj;
    private String descricao_motivo_situacao_cadastral;
    private String nome_fantasia;
    private String descricao_situacao_cadastral;

    // getters e setters
}
