package com.my.selfimprovement.service.token;

import com.my.selfimprovement.security.SpringUserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpringSecurityJwtService implements JwtService {

    private final JwtEncoder encoder;

    private final JwtDecoder decoder;

    @Override
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        SpringUserDetailsImpl principal = (SpringUserDetailsImpl) authentication.getPrincipal();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self-improvement-social")
                .issuedAt(now)
                .expiresAt(now.plus(60, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim(CLAIM_SCOPE, scope)
                .claim(CLAIM_USER_ID, principal.getUser().getId())
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public Instant getExpiration(String jwt) {
        return decoder.decode(jwt).getExpiresAt();
    }

}
