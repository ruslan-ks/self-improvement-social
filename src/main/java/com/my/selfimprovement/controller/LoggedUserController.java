package com.my.selfimprovement.controller;

import com.my.selfimprovement.dto.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class LoggedUserController {

    @GetMapping("/json")
    public ResponseEntity<ResponseBody> userData(@AuthenticationPrincipal Jwt jwt) {
        log.info("GET /user/json: principal jwt claims: {}", jwt.getClaims());
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.OK)
                .data(Map.of("username", jwt.getClaim("sub"), "scope", jwt.getClaim("scope")))
                .build();
        return ResponseEntity.ok(responseBody);
    }

}
