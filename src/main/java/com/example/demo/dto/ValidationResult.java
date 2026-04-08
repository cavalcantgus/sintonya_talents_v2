package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class ValidationResult {
    private boolean valid;
    private String code;
    private String message;

    public static ValidationResult success(boolean valid, String code, String message) {
        return new ValidationResult(true, code, message);
    }

    public static ValidationResult error(boolean valid, String code, String message) {
        return new ValidationResult(valid, code, message);
    }
}
