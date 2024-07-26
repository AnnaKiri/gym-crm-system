package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.UserDAO;
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
        log.debug("Check user authentication with username = {}", username);
        return userDAO.getByUsernameAndPassword(username, password);
    }
}