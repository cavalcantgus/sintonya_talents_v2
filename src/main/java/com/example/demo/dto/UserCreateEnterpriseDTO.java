package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateEnterpriseDTO {
    private String enterpriseName;
    private String owner;
    private String email;
    private String cnpj;
    private String socialReason;
    private String contact;
}
