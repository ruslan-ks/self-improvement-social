package com.my.selfimprovement.controller;

import com.my.selfimprovement.dto.response.ResponseBody;
import com.my.selfimprovement.dto.request.UserLoginRequest;
import com.my.selfimprovement.service.token.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<ResponseBody> login(@RequestBody UserLoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        log.debug("Login request for user: {}", auth.getName());
        String jwt = jwtService.generateToken(auth);

        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.OK)
                .timestamp(Instant.now())
                .data(Map.of("jwt", jwt))
                .build();
        return ResponseEntity.ok(responseBody);
    }

}
