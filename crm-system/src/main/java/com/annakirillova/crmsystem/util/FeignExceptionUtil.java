package com.annakirillova.crmsystem.util;

import com.annakirillova.crmsystem.exception.AppException;
import com.annakirillova.crmsystem.exception.AuthenticationException;
import com.annakirillova.crmsystem.exception.FeignCircuitBreakerException;
import com.annakirillova.crmsystem.exception.FeignConnectionException;
import com.annakirillova.crmsystem.exception.FeignServiceUnavailableException;
import com.annakirillova.crmsystem.exception.FeignTimeoutException;
import com.annakirillova.crmsystem.exception.FeignUnknownException;
import com.annakirillova.crmsystem.exception.NotFoundException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.validation.ValidationException;
import lombok.experimental.UtilityClass;
import org.springframework.security.access.AccessDeniedException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;

@UtilityClass
public class FeignExceptionUtil {

    public static Map<String, String> getExceptionMessages(String serviceName, Throwable t) {
        return Map.of(
                "400", serviceName + ", Validation error: " + t.getMessage(),
                "401", serviceName + ", Wrong credentials",
                "403", serviceName + ", Forbidden request",
                "404", serviceName + ", Endpoint not found " + t.getMessage(),
                "500", serviceName + ", Internal server error. Thy again later",
                "503", serviceName + ", Service unavailable. Thy again later",
                "SocketTimeoutException", serviceName + ", Service unavailable. Thy again later",
                "ConnectException", serviceName + ", Service unavailable. Thy again later",
                "CallNotPermittedException", serviceName + ", Service unavailable. Thy again later",
                "Throwable", serviceName + ", Unknown error occurred."
        );
    }

    public Throwable handleFeignException(Throwable t, Map<String, String> exceptionMessages) {
        switch (t) {
            case FeignException feignException -> {
                int statusCode = feignException.status();
                String message = exceptionMessages.get(String.valueOf(statusCode));

                return switch (statusCode) {
                    case 400 -> new ValidationException(message);
                    case 401 -> new AuthenticationException(message);
                    case 403 -> new AccessDeniedException(message);
                    case 404 -> new NotFoundException(message);
                    case 500 -> new AppException(message);
                    case 503 -> new FeignServiceUnavailableException(message);
                    default -> new FeignUnknownException("Unhandled status code: " + statusCode);
                };
            }
            case SocketTimeoutException socketTimeoutException -> {
                String message = exceptionMessages.get("SocketTimeoutException");
                return new FeignTimeoutException(message);
            }
            case ConnectException connectException -> {
                String message = exceptionMessages.get("ConnectException");
                return new FeignConnectionException(message);
            }
            case CallNotPermittedException callNotPermittedException -> {
                String message = exceptionMessages.get("CallNotPermittedException");
                return new FeignCircuitBreakerException(message);
            }
            case null, default -> {
                String message = exceptionMessages.get("Throwable");
                return new FeignUnknownException(message);
            }
        }
    }
}
