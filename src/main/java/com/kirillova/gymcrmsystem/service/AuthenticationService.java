package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.UserDAO;
import com.kirillova.gymcrmsystem.error.AuthenticationException;
import com.kirillova.gymcrmsystem.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserDAO userDAO;

    public User getAuthenticatedUser(String username, String password) {
        log.debug("Get user with username for authentication = {}", username);
        return userDAO.getByUsernameAndPassword(username, password);
    }

    public void checkAuthenticatedUser(String username, String password) {
        log.debug("Check user authentication with username = {}", username);
        if (getAuthenticatedUser(username, password) == null) {
            throw new AuthenticationException("User with this credentials doesn't exist");
        }
    }
}