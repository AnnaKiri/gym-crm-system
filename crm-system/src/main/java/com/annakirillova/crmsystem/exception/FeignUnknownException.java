package com.annakirillova.crmsystem.exception;

public class FeignUnknownException extends RuntimeException {
    public FeignUnknownException(String msg) {
        super(msg);
    }
}
