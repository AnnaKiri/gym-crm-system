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

    public void userAuthentication(String username, String password) {
        log.debug("Check user authentication with username = " + username);
        User authenticatedUser = userDAO.getByUsernameAndPassword(username, password);
        if (authenticatedUser == null) {
            throw new AuthenticationException("User with this credentials doesn't exist");
        }
    }
}