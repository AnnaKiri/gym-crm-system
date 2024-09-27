package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.web.AuthUser;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public String getJwtToken() {
        return AuthUser.getJwtToken();
    }
}
