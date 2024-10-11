package com.annakirillova.crmsystem.exception;

public class DataConflictException extends RuntimeException {
    public DataConflictException(String msg) {
        super(msg);
    }

    public static DataConflictException missingToken() {
        return new DataConflictException("JWT token is missing");
    }
}