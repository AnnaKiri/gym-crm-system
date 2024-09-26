package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.web.AuthUser;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public String getJwtToken() {
        return AuthUser.getJwtToken();
    }
}
