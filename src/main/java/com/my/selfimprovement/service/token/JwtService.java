package com.my.selfimprovement.service.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

public interface JwtService {
    String CLAIM_SCOPE = "scope";
    String CLAIM_USER_ID = "userId";

    String generateToken(Authentication authentication);
    Instant getExpiration(String jwt);
    long getUserId(Jwt jwt);
}
