package com.my.selfimprovement.service.token;

import org.springframework.security.core.Authentication;

import java.time.Instant;

public interface JwtService {
    String generateToken(Authentication authentication);
    Instant getExpiration(String jwt);
}
