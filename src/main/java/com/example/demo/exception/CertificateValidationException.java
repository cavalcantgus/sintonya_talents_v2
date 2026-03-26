package com.example.demo.exception;

import com.example.demo.enums.CertificateError;

public class CertificateValidationException extends RuntimeException {

    private final CertificateError error;

    public CertificateValidationException(CertificateError error) {
        super(error.getMessage());
        this.error = error;
    }

    public CertificateError getError() {
        return error;
    }
}
