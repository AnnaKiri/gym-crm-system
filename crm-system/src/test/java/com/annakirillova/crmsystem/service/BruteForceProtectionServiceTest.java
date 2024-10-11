package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.models.LoginAttempt;
import com.annakirillova.crmsystem.repository.LoginAttemptRepository;
import com.annakirillova.crmsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.annakirillova.crmsystem.UserTestData.USER_1;
import static com.annakirillova.crmsystem.UserTestData.USER_1_USERNAME;
import static com.annakirillova.crmsystem.service.BruteForceProtectionService.BLOCK_DURATION_MINUTES;
import static com.annakirillova.crmsystem.service.BruteForceProtectionService.MAX_ATTEMPTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BruteForceProtectionServiceTest {

    @Mock
    private LoginAttemptRepository loginAttemptRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BruteForceProtectionService bruteForceProtectionService;

    @Test
    void testResetBlock() {
        String username = "testuser";
        when(loginAttemptRepository.deleteByUsername(username)).thenReturn(1);

        bruteForceProtectionService.resetBlock(username);

        verify(loginAttemptRepository, times(1)).deleteByUsername(username);
    }

    @Test
    void testLoginFailedWithNewAttempt() {
        when(userRepository.findByUsername(USER_1_USERNAME)).thenReturn(Optional.of(USER_1));
        when(loginAttemptRepository.findByUsername(USER_1_USERNAME)).thenReturn(Optional.empty());

        bruteForceProtectionService.loginFailed(USER_1_USERNAME);

        verify(loginAttemptRepository, times(1)).save(any(LoginAttempt.class));
    }

    @Test
    void testLoginFailedWithExistingAttempt() {
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setAttempts(MAX_ATTEMPTS - 1);

        when(userRepository.findByUsername(USER_1_USERNAME)).thenReturn(Optional.of(USER_1));
        when(loginAttemptRepository.findByUsername(USER_1_USERNAME)).thenReturn(Optional.of(loginAttempt));

        bruteForceProtectionService.loginFailed(USER_1_USERNAME);

        assertEquals(3, loginAttempt.getAttempts());
        verify(loginAttemptRepository, times(1)).save(loginAttempt);
    }

    @Test
    void testLoginFailedAndBlockUser() {
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setAttempts(MAX_ATTEMPTS);

        when(userRepository.findByUsername(USER_1_USERNAME)).thenReturn(Optional.of(USER_1));
        when(loginAttemptRepository.findByUsername(USER_1_USERNAME)).thenReturn(Optional.of(loginAttempt));

        bruteForceProtectionService.loginFailed(USER_1_USERNAME);

        assertNotNull(loginAttempt.getBlockedUntil());
        verify(loginAttemptRepository, times(1)).save(loginAttempt);
    }

    @Test
    void testIsBlockedUserNotBlocked() {
        LoginAttempt loginAttempt = new LoginAttempt();

        when(loginAttemptRepository.findByUsername(USER_1_USERNAME)).thenReturn(Optional.of(loginAttempt));

        boolean isBlocked = bruteForceProtectionService.isBlocked(USER_1_USERNAME);

        assertFalse(isBlocked);
        verify(loginAttemptRepository, times(1)).findByUsername(USER_1_USERNAME);
    }

    @Test
    void testIsBlockedUserCurrentlyBlocked() {
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setBlockedUntil(LocalDateTime.now().plusMinutes(BLOCK_DURATION_MINUTES));

        when(loginAttemptRepository.findByUsername(USER_1_USERNAME)).thenReturn(Optional.of(loginAttempt));

        boolean isBlocked = bruteForceProtectionService.isBlocked(USER_1_USERNAME);

        assertTrue(isBlocked);
        verify(loginAttemptRepository, times(1)).findByUsername(USER_1_USERNAME);
    }

    @Test
    void testIsBlockedBlockExpired() {
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setBlockedUntil(LocalDateTime.now().minusMinutes(1));

        when(loginAttemptRepository.findByUsername(USER_1_USERNAME)).thenReturn(Optional.of(loginAttempt));

        boolean isBlocked = bruteForceProtectionService.isBlocked(USER_1_USERNAME);

        assertFalse(isBlocked);
        verify(loginAttemptRepository, times(1)).findByUsername(USER_1_USERNAME);
        verify(loginAttemptRepository, times(1)).deleteByUsername(USER_1_USERNAME);
    }

    @Test
    void testIsBlockedNoAttemptsFound() {
        when(loginAttemptRepository.findByUsername(USER_1_USERNAME)).thenReturn(Optional.empty());

        boolean isBlocked = bruteForceProtectionService.isBlocked(USER_1_USERNAME);

        assertFalse(isBlocked);
        verify(loginAttemptRepository, times(1)).findByUsername(USER_1_USERNAME);
    }
}
