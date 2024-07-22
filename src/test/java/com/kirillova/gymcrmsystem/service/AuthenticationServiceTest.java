package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.UserDAO;
import com.kirillova.gymcrmsystem.error.AuthenticationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void testUserAuthenticationSuccess() {
        when(userDAO.getByUsernameAndPassword(USER_1.getUsername(), USER_1.getPassword())).thenReturn(USER_1);
        authenticationService.userAuthentication(USER_1.getUsername(), USER_1.getPassword());
    }

    @Test
    void testUserAuthenticationFailure() {
        assertThrows(AuthenticationException.class, () -> {
            authenticationService.userAuthentication("test", "test");
        });
    }
}
