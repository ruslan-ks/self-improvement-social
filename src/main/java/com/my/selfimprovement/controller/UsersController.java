package com.my.selfimprovement.controller;

import com.my.selfimprovement.dto.mapper.UserMapper;
import com.my.selfimprovement.dto.response.DetailedUserResponse;
import com.my.selfimprovement.dto.response.ShortUserResponse;
import com.my.selfimprovement.dto.response.ResponseBody;
import com.my.selfimprovement.dto.request.UserRegistrationRequest;
import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.service.UserService;
import com.my.selfimprovement.util.LoadedFile;
import com.my.selfimprovement.util.validation.UserRegistrationRequestValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final UserService userService;

    private final PasswordEncoder encoder;

    private final UserRegistrationRequestValidator userRegistrationRequestValidator;

    private final UserMapper userMapper;

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

    @PutMapping("/{id}/avatar")
    public ResponseEntity<ResponseBody> uploadAvatar(@PathVariable("id") long userId,
                                                     @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            ResponseBody badRequestResponseBody = ResponseBody.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Avatar file not attached")
                    .build();
            return ResponseEntity.badRequest().body(badRequestResponseBody);
        }
        userService.setUserAvatar(file, userId);
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.OK)
                .message("Avatar successfully uploaded")
                .build();
        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping("/{id}/avatar")
    public ResponseEntity<Resource> getAvatar(@PathVariable("id") long userId) throws IOException {
        LoadedFile avatarLoadedFile = userService.getUserAvatar(userId);
        Resource avatarResource = new ByteArrayResource(avatarLoadedFile.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(getMediaType(avatarLoadedFile.getPath()))
                .body(avatarResource);
    }

    private MediaType getMediaType(Path filePath) throws IOException {
        return MediaType.parseMediaType(Files.probeContentType(filePath));
    }

}
