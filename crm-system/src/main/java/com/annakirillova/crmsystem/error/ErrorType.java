package com.annakirillova.crmsystem.error;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    APP_ERROR("Application error", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_DATA("Wrong data", HttpStatus.UNPROCESSABLE_ENTITY),
    BAD_REQUEST("Bad request", HttpStatus.BAD_REQUEST),
    DATA_CONFLICT("DB data conflict", HttpStatus.CONFLICT),
    NOT_FOUND("Wrong data in request", HttpStatus.NOT_FOUND),
    AUTH_ERROR("Authorization error", HttpStatus.FORBIDDEN),
    UNAUTHORIZED("Request unauthorized", HttpStatus.UNAUTHORIZED),
    SERVICE_UNAVAILABLE("Service Unavailable", HttpStatus.SERVICE_UNAVAILABLE),
    FORBIDDEN("Request forbidden", HttpStatus.FORBIDDEN),
    FEIGN_TIMEOUT("Timeout occurred while calling Feign client", HttpStatus.GATEWAY_TIMEOUT),
    FEIGN_CONNECTION_ERROR("Connection error occurred while calling Feign client", HttpStatus.BAD_GATEWAY);

    public final String title;
    public final HttpStatus status;

    ErrorType(String title, HttpStatus status) {
        this.title = title;
        this.status = status;
    }
}
