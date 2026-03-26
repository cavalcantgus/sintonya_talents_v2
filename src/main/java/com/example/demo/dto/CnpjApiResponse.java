package com.example.demo.dto;

import lombok.Getter;

@Getter
public class CnpjApiResponse {
    private String cnpj;
    private String razao_social;
    private String nome_fantasia;
    private String descricao_situacao_cadastral;

    // getters e setters
}
