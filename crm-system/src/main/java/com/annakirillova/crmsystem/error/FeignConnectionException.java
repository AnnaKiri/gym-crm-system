package com.annakirillova.crmsystem.error;

public class FeignConnectionException extends AppException {
    public FeignConnectionException(String msg) {
        super(msg);
    }
}
