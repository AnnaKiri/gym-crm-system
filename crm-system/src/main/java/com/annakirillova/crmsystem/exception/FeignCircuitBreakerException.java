package com.annakirillova.crmsystem.exception;

public class FeignCircuitBreakerException extends RuntimeException {
    public FeignCircuitBreakerException(String msg) {
        super(msg);
    }
}
