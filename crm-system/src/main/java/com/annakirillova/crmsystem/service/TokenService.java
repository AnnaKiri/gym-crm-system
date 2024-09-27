package com.annakirillova.crmsystem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void invalidateToken(String token, long expirationTimeInSeconds) {
        log.debug("Token {} has been marked as invalid.", token);
        redisTemplate.opsForValue().set(token, "invalidated", expirationTimeInSeconds, TimeUnit.SECONDS);
    }

    public boolean isTokenInvalid(String token) {
        log.debug("Checking if token {} is in the invalidated token storage.", token);
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
