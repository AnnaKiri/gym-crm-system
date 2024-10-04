package com.annakirillova.crmsystem.error;

public class FeignServiceUnavailableException extends AppException {
    public FeignServiceUnavailableException(String msg) {
        super(msg);
    }
}
