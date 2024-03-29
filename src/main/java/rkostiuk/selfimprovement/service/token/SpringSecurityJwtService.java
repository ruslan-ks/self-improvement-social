package rkostiuk.selfimprovement.service.token;

import rkostiuk.selfimprovement.security.SpringUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
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
        var principal = (SpringUserDetails) authentication.getPrincipal();
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

    @Override
    public long getUserId(Jwt jwt) {
        return jwt.getClaim(CLAIM_USER_ID);
    }

}
