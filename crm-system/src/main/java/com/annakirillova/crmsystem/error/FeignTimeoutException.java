package com.annakirillova.crmsystem.error;

public class FeignTimeoutException extends AppException {
    public FeignTimeoutException(String msg) {
        super(msg);
    }
}
