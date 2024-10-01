package com.annakirillova.crmsystem.feign;

import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "keycloak-service", url = "${keycloak.url}/admin/realms/${keycloak.realm}")
public interface KeycloakFeignClient {

    @PostMapping("/users")
    ResponseEntity<Void> createUser(@RequestHeader("Authorization") String token, @RequestBody KeycloakUserDto user);
}
