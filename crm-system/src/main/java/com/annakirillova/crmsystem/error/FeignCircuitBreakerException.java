package com.annakirillova.crmsystem.error;

public class FeignCircuitBreakerException extends AppException {
    public FeignCircuitBreakerException(String msg) {
        super(msg);
    }
}
