package com.my.selfimprovement.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @GetMapping("/json")
    public Map<String, String> userData(@AuthenticationPrincipal Jwt jwt) {
        log.info("GET /user/json: principal jwt claims: {}", jwt.getClaims());
        return Map.of("username", jwt.getClaim("sub"),
                "scope", jwt.getClaim("scope"));
    }

}
