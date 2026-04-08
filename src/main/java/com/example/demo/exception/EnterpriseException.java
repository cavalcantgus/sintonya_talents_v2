package com.example.demo.exception;

import com.example.demo.entities.Enterprise;

public class EnterpriseException extends RuntimeException {

    public EnterpriseException(String message) {
        super(message);
    }
}
