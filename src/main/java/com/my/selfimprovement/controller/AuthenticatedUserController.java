package com.my.selfimprovement.controller;

import com.my.selfimprovement.dto.response.ResponseBody;
import com.my.selfimprovement.service.UserService;
import com.my.selfimprovement.service.token.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class AuthenticatedUserController {

    private final UserService userService;

    @PutMapping("/avatar")
    public ResponseEntity<ResponseBody> uploadAvatar(@RequestParam("file") MultipartFile file,
                                                     @AuthenticationPrincipal Jwt jwt) {
        if (file.isEmpty()) {
            ResponseBody badRequestResponseBody = ResponseBody.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Avatar file not attached")
                    .build();
            return ResponseEntity.badRequest().body(badRequestResponseBody);
        }

        long userId = jwt.getClaim(JwtService.CLAIM_USER_ID);
        userService.setAvatar(file, userId);
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.OK)
                .message("Avatar successfully uploaded")
                .build();
        return ResponseEntity.ok().body(responseBody);
    }

    @DeleteMapping("/avatar")
    public ResponseEntity<ResponseBody> deleteAvatar(@AuthenticationPrincipal Jwt jwt) {
        long userId = jwt.getClaim(JwtService.CLAIM_USER_ID);
        try {
            userService.removeAvatar(userId);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }

        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.OK)
                .message("Avatar successfully deleted")
                .build();
        return ResponseEntity.ok().body(responseBody);
    }

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
