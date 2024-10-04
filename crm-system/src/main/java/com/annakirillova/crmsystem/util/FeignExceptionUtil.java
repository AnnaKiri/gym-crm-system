package com.annakirillova.crmsystem.util;

import com.annakirillova.crmsystem.error.AppException;
import com.annakirillova.crmsystem.error.AuthenticationException;
import com.annakirillova.crmsystem.error.FeignCircuitBreakerException;
import com.annakirillova.crmsystem.error.FeignConnectionException;
import com.annakirillova.crmsystem.error.FeignServiceUnavailableException;
import com.annakirillova.crmsystem.error.FeignTimeoutException;
import com.annakirillova.crmsystem.error.FeignUnknownException;
import com.annakirillova.crmsystem.error.NotFoundException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.validation.ValidationException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;

@UtilityClass
@Slf4j
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

    public void handleFeignException(Throwable t, Map<String, String> exceptionMessages) {
        switch (t) {
            case FeignException feignException -> {
                int statusCode = feignException.status();
                String message = exceptionMessages.get(String.valueOf(statusCode));

                switch (statusCode) {
                    case 400:
                        log.error(message);
                        throw new ValidationException(message);
                    case 401:
                        log.error(message);
                        throw new AuthenticationException(message);
                    case 403:
                        log.error(message);
                        throw new AccessDeniedException(message);
                    case 404:
                        log.error(message);
                        throw new NotFoundException(message);
                    case 500:
                        log.error(message);
                        throw new AppException(message);
                    case 503:
                        log.error(message);
                        throw new FeignServiceUnavailableException(message);
                    default:
                        log.error(message);
                        throw new FeignUnknownException("Unhandled status code: " + statusCode);
                }
            }
            case SocketTimeoutException socketTimeoutException -> {
                String message = exceptionMessages.get("SocketTimeoutException");
                log.error(message);
                throw new FeignTimeoutException(message);
            }
            case ConnectException connectException -> {
                String message = exceptionMessages.get("ConnectException");
                log.error(message);
                throw new FeignConnectionException(message);
            }
            case CallNotPermittedException callNotPermittedException -> {
                String message = exceptionMessages.get("CallNotPermittedException");
                log.error(message);
                throw new FeignCircuitBreakerException(message);
            }
            case null, default -> {
                String message = exceptionMessages.get("Throwable");
                log.error(message);
                throw new FeignUnknownException(message);
            }
        }
    }
}
