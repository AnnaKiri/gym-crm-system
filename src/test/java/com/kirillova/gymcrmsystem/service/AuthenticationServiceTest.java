package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void testGetAuthenticatedUserSuccess() {
        when(userRepository.getByUsernameAndPassword(USER_1.getUsername(), USER_1.getPassword())).thenReturn(USER_1);
        authenticationService.checkAuthenticatedUser(USER_1.getUsername(), USER_1.getPassword());
    }

    @Test
    void testGetAuthenticatedUserFailure() {
        assertNull(authenticationService.getAuthenticatedUser("test", "test"));
    }
}
