package com.my.selfimprovement.controller;

import com.my.selfimprovement.dto.request.UserUpdateRequest;
import com.my.selfimprovement.dto.response.DetailedUserResponse;
import com.my.selfimprovement.dto.response.ResponseBody;
import com.my.selfimprovement.service.UserService;
import com.my.selfimprovement.service.token.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

    @Autowired
    private final MessageSource messageSource;

    @PatchMapping
    public ResponseEntity<ResponseBody> update(@RequestBody @Valid UserUpdateRequest userUpdateRequest,
                                               @AuthenticationPrincipal Jwt jwt) {
        long userId = jwt.getClaim(JwtService.CLAIM_USER_ID);
        DetailedUserResponse updatedUser = userService.update(userId, userUpdateRequest);
        String message = messageSource.getMessage("user.updated", null, LocaleContextHolder.getLocale());
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.OK)
                .data(Map.of("user", updatedUser))
                .message(message)
                .build();
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/avatar")
    public ResponseEntity<ResponseBody> uploadAvatar(@RequestParam("file") MultipartFile file,
                                                     @AuthenticationPrincipal Jwt jwt) {
        if (file.isEmpty()) {
            String message = messageSource.getMessage("user.avatar.notAttached", null,
                    LocaleContextHolder.getLocale());
            ResponseBody badRequestResponseBody = ResponseBody.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(message)
                    .build();
            return ResponseEntity.badRequest().body(badRequestResponseBody);
        }

        long userId = jwt.getClaim(JwtService.CLAIM_USER_ID);
        userService.setAvatar(file, userId);
        String message = messageSource.getMessage("user.avatar.uploaded", null,
                LocaleContextHolder.getLocale());
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.OK)
                .message(message)
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

        String message = messageSource.getMessage("user.avatar.deleted", null,
                LocaleContextHolder.getLocale());
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.OK)
                .message(message)
                .build();
        return ResponseEntity.ok().body(responseBody);
    }

}
