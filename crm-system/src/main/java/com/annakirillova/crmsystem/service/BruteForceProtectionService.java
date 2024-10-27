package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.models.LoginAttempt;
import com.annakirillova.crmsystem.repository.LoginAttemptRepository;
import com.annakirillova.crmsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BruteForceProtectionService {
    public static final int MAX_ATTEMPTS = 3;
    public static final long BLOCK_DURATION_MINUTES = 5;
    public static final String BLOCK_MESSAGE = "User is blocked due to multiple failed login attempts. Please try again later.";

    private final LoginAttemptRepository loginAttemptRepository;
    private final UserRepository userRepository;

    @Transactional
    public void resetBlock(String username) {
        loginAttemptRepository.deleteByUsername(username);

        log.info("Block reset for user: {}", username);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loginFailed(String username) {
        log.info("Login failed for user: {}", username);

        userRepository.findByUsername(username).ifPresent(user -> {
            LoginAttempt loginAttempt = loginAttemptRepository.findByUsername(username).orElse(new LoginAttempt());
            loginAttempt.setUser(user);
            loginAttempt.setAttempts(loginAttempt.getAttempts() + 1);

            if (loginAttempt.getAttempts() >= MAX_ATTEMPTS) {
                loginAttempt.setBlockedUntil(LocalDateTime.now().plusMinutes(BLOCK_DURATION_MINUTES));

                log.warn("User {} has been blocked for {} minutes due to too many failed login attempts", username, BLOCK_DURATION_MINUTES);
            }

            loginAttemptRepository.save(loginAttempt);
        });
    }

    @Transactional
    public boolean isBlocked(String username) {
        log.debug("Checking if user {} is blocked", username);

        Optional<LoginAttempt> optionalLoginAttempt = loginAttemptRepository.findByUsername(username);

        if (optionalLoginAttempt.isPresent()) {
            LocalDateTime blockedUntil = optionalLoginAttempt.get().getBlockedUntil();

            log.debug("User {} block status found. Blocked until: {}", username, blockedUntil);

            if (blockedUntil == null) {
                log.debug("User {} is not blocked", username);

                return false;
            } else if (blockedUntil.isBefore(LocalDateTime.now())) {
                log.debug("User {} block has expired. Resetting block.", username);

                resetBlock(username);
                return false;
            } else {
                log.warn("User {} is currently blocked until {}", username, blockedUntil);

                return true;
            }
        } else {
            log.debug("No login attempts found for user {}", username);
        }
        return false;
    }
}
