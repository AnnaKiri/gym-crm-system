package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.models.LoginAttempt;
import com.annakirillova.crmsystem.repository.LoginAttemptRepository;
import com.annakirillova.crmsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BruteForceProtectionService {
    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_DURATION_MINUTES = 5;

    private final LoginAttemptRepository loginAttemptRepository;
    private final UserRepository userRepository;

    @Transactional
    public void resetBlock(String username) {
        loginAttemptRepository.deleteByUsername(username);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loginFailed(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            LoginAttempt loginAttempt = loginAttemptRepository.findByUsername(username).orElse(new LoginAttempt());
            loginAttempt.setUser(user);
            loginAttempt.setAttempts(loginAttempt.getAttempts() + 1);

            if (loginAttempt.getAttempts() >= MAX_ATTEMPTS) {
                loginAttempt.setBlockedUntil(LocalDateTime.now().plusMinutes(BLOCK_DURATION_MINUTES));
            }

            loginAttemptRepository.save(loginAttempt);
        });
    }

    @Transactional
    public boolean isBlocked(String username) {
        Optional<LoginAttempt> optionalLoginAttempt = loginAttemptRepository.findByUsername(username);
        if (optionalLoginAttempt.isPresent()) {
            LocalDateTime blockedUntil = optionalLoginAttempt.get().getBlockedUntil();
            if (blockedUntil == null) {
                return false;
            } else if (blockedUntil.isBefore(LocalDateTime.now())) {
                resetBlock(username);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
