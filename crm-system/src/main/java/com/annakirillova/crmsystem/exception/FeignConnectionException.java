package com.annakirillova.crmsystem.exception;

public class FeignConnectionException extends RuntimeException {
    public FeignConnectionException(String msg) {
        super(msg);
    }
}
