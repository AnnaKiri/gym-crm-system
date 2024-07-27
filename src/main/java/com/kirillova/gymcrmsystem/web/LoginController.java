package com.kirillova.gymcrmsystem.web;

import com.kirillova.gymcrmsystem.error.AuthenticationException;
import com.kirillova.gymcrmsystem.service.AuthenticationService;
import com.kirillova.gymcrmsystem.to.UserTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Login Controller", description = "Handles user login")
public class LoginController {
    static final String REST_URL = "/login";

    @Autowired
    protected AuthenticationService authenticationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login user", description = "Logs in a user with provided username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    public void login(@Valid @RequestBody UserTo userTo) {
        log.info("Login user with username={}", userTo.getUsername());
        if (authenticationService.getAuthenticatedUser(userTo.getUsername(), userTo.getPassword()) == null) {
            throw new AuthenticationException("User with this credentials doesn't exist");
        }
    }
}
