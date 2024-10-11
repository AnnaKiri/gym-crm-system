package com.annakirillova.crmsystem.exception;

public class FeignServiceUnavailableException extends RuntimeException {
    public FeignServiceUnavailableException(String msg) {
        super(msg);
    }
}
