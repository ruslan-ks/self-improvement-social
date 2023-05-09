package com.my.selfimprovement.controller;

import com.my.selfimprovement.dto.mapper.UserMapper;
import com.my.selfimprovement.dto.request.UserUpdateRequest;
import com.my.selfimprovement.dto.response.DetailedUserResponse;
import com.my.selfimprovement.dto.response.ShortUserResponse;
import com.my.selfimprovement.dto.response.ResponseBody;
import com.my.selfimprovement.dto.request.UserRegistrationRequest;
import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.service.UserService;
import com.my.selfimprovement.service.token.JwtService;
import com.my.selfimprovement.util.HttpUtils;
import com.my.selfimprovement.util.validation.UserRegistrationRequestValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final UserService userService;

    private final PasswordEncoder encoder;

    private final UserRegistrationRequestValidator userRegistrationRequestValidator;

    private final UserMapper userMapper;

    private final MessageSource messageSource;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @PostMapping
    public ResponseEntity<ResponseBody> create(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {
        userRegistrationRequestValidator.validate(userRegistrationRequest);
        User user = userMapper.toUser(userRegistrationRequest);
        user.setPassword(encoder.encode(user.getPassword()));
        userService.save(user);

        DetailedUserResponse detailedUserResponse = userMapper.toDetailedUserResponse(user);
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.CREATED)
                .data(Map.of("user", detailedUserResponse))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping
    public ResponseEntity<ResponseBody> getActiveUsersPage(Pageable pageable) {
        log.info("Pageable: {}", pageable);
        List<ShortUserResponse> users = userService.findActiveUsersPage(pageable)
                .map(userMapper::toShortUserResponse)
                .toList();
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.OK)
                .data(Map.of("users", users))
                .build();
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/count")
    public ResponseEntity<ResponseBody> count() {
        long count = userService.count();
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.OK)
                .data(Map.of("count", count))
                .build();
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseBody> getById(@PathVariable("id") long userId) {
        return userService.findById(userId)
                .map(userMapper::toDetailedUserResponse)
                .map(this::buildDetailedUserResponse)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<ResponseBody> buildDetailedUserResponse(DetailedUserResponse user) {
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.OK)
                .data(Map.of("user", user))
                .build();
        return ResponseEntity.ok().body(responseBody);
    }

    @PatchMapping
    public ResponseEntity<ResponseBody> update(@RequestBody @Valid UserUpdateRequest userUpdateRequest,
                                               @AuthenticationPrincipal Jwt jwt) {
        long userId = jwt.getClaim(JwtService.CLAIM_USER_ID);
        User updatedUser = userService.update(userId, userUpdateRequest);
        DetailedUserResponse updatedUserResponse = userMapper.toDetailedUserResponse(updatedUser);
        String message = messageSource.getMessage("user.updated", null, LocaleContextHolder.getLocale());
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.OK)
                .data(Map.of("user", updatedUserResponse))
                .message(message)
                .build();
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{id}/avatar")
    public ResponseEntity<Resource> getAvatar(@PathVariable("id") long userId) {
        return userService.getAvatar(userId)
                .map(HttpUtils::buildInlineFileResponse)
                .orElseGet(() -> ResponseEntity.notFound().build());
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
