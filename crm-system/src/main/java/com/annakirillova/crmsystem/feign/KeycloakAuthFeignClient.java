package com.annakirillova.crmsystem.feign;

import com.annakirillova.crmsystem.config.FeignClientConfig;
import com.annakirillova.crmsystem.dto.TokenResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "keycloak-auth", url = "${keycloak.url}/realms/${keycloak.realm}/protocol/openid-connect", configuration = FeignClientConfig.class)
public interface KeycloakAuthFeignClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenResponseDto loginUser(Map<String, ?> request);

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<Void> logoutUser(Map<String, ?> request);
}
