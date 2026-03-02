package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {
    private String candidateName;
    private String cpf;
    private String email;
    private String contact;
    private String password;
}
