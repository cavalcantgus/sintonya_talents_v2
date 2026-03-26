package com.example.demo.services;

import com.example.demo.dto.CnpjApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CnpjService {

    private final WebClient webClient;

    public CnpjService() {
        this.webClient = WebClient.create("https://brasilapi.com.br/api");
    }

    public CnpjApiResponse buscarCnpj(String cnpj) {
        try {
            return webClient
                    .get()
                    .uri("/cnpj/v1/{cnpj}", cnpj)
                    .retrieve()
                    .bodyToMono(CnpjApiResponse.class)
                    .block();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar CNPJ");
        }
    }
}
