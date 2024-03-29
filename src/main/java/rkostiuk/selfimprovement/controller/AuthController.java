package rkostiuk.selfimprovement.controller;

import rkostiuk.selfimprovement.dto.response.ResponseBody;
import rkostiuk.selfimprovement.dto.request.UserLoginRequest;
import rkostiuk.selfimprovement.service.token.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseBody login(@RequestBody UserLoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        log.debug("Login request for user: {}", auth.getName());
        String jwt = jwtService.generateToken(auth);
        return ResponseBody.ok(Map.of("idToken", jwt, "expiresAt", jwtService.getExpiration(jwt)));
    }

}
