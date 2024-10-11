package com.annakirillova.crmsystem.exception;

public class FeignTimeoutException extends RuntimeException {
    public FeignTimeoutException(String msg) {
        super(msg);
    }
}
