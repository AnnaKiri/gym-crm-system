package com.kirillova.gymcrmsystem.web;

import com.kirillova.gymcrmsystem.service.AuthenticationService;
import com.kirillova.gymcrmsystem.to.UserTo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = LoginController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {
    static final String REST_URL = "/login";

    @Autowired
    protected AuthenticationService authenticationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void login(@Valid @RequestBody UserTo userTo) {
        log.info("Login user with username={}", userTo.getUsername());
        authenticationService.getAuthenticatedUser(userTo.getUsername(), userTo.getPassword());
    }
}
