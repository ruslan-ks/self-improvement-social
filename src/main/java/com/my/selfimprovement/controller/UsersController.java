package com.my.selfimprovement.controller;

import com.my.selfimprovement.dto.mapper.UserActivityMapper;
import com.my.selfimprovement.dto.mapper.UserMapper;
import com.my.selfimprovement.dto.request.UserUpdateRequest;
import com.my.selfimprovement.dto.response.DetailedUserResponse;
import com.my.selfimprovement.dto.response.ShortUserActivityResponse;
import com.my.selfimprovement.dto.response.ShortUserResponse;
import com.my.selfimprovement.dto.response.ResponseBody;
import com.my.selfimprovement.dto.request.UserRegistrationRequest;
import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.service.ActivityService;
import com.my.selfimprovement.service.UserService;
import com.my.selfimprovement.service.token.JwtService;
import com.my.selfimprovement.util.HttpUtils;
import com.my.selfimprovement.util.exception.AvatarNotFoundException;
import com.my.selfimprovement.util.validation.UserRegistrationRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
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

    private final ActivityService activityService;

    private final PasswordEncoder encoder;

    private final UserRegistrationRequestValidator userRegistrationRequestValidator;

    private final UserMapper userMapper;

    private final UserActivityMapper userActivityMapper;

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
        List<ShortUserResponse> users = userService.findActiveUsersPage(pageable)
                .map(userMapper::toShortUserResponse)
                .toList();
        return HttpUtils.ok(Map.of("users", users));
    }

    @GetMapping("/count")
    public ResponseEntity<ResponseBody> count() {
        long count = userService.count();
        return HttpUtils.ok(Map.of("count", count));
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseBody> getById(@PathVariable("id") long userId) {
        User user = userService.findByIdOrElseThrow(userId);
        DetailedUserResponse userDto = userMapper.toDetailedUserResponse(user);
        return HttpUtils.ok(Map.of("user", userDto));
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
                .orElseThrow(() -> new AvatarNotFoundException("Avatar for user with id " + userId + " not found"));
    }

    @PutMapping("/avatar")
    public ResponseEntity<ResponseBody> uploadAvatar(@RequestParam("file") MultipartFile file,
                                                     @AuthenticationPrincipal Jwt jwt) {
        if (file.isEmpty()) {
            String message = messageSource.getMessage("user.avatar.notAttached", null,
                    LocaleContextHolder.getLocale());
            return HttpUtils.badRequest(message);
        }

        long userId = jwt.getClaim(JwtService.CLAIM_USER_ID);
        userService.setAvatar(file, userId);
        String message = messageSource.getMessage("user.avatar.uploaded", null,
                LocaleContextHolder.getLocale());
        return HttpUtils.ok(message);
    }

    @DeleteMapping("/avatar")
    public ResponseEntity<ResponseBody> deleteAvatar(@AuthenticationPrincipal Jwt jwt) {
        long userId = jwt.getClaim(JwtService.CLAIM_USER_ID);
        userService.removeAvatar(userId);

        String message = messageSource.getMessage("user.avatar.deleted", null,
                LocaleContextHolder.getLocale());
        return HttpUtils.ok(message);
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<ResponseBody> getFollowersCount(@PathVariable long userId) {
        long count = userService.getFollowersCount(userId);
        return HttpUtils.ok(Map.of("followersCount", count));
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<ResponseBody> getUserFollowersPage(@PathVariable long userId, Pageable pageable) {
        List<ShortUserResponse> followers = userService.getFollowersPage(userId, pageable)
                .map(userMapper::toShortUserResponse)
                .toList();
        return HttpUtils.ok(Map.of("followers", followers));
    }

    @GetMapping("/{userId}/followings/count")
    public ResponseEntity<ResponseBody> getFollowingsCount(@PathVariable long userId) {
        long count = userService.getFollowingsCount(userId);
        return HttpUtils.ok(Map.of("followingsCount", count));
    }


    @GetMapping("/{userId}/followings")
    public ResponseEntity<ResponseBody> getFollowingsPage(@PathVariable long userId, Pageable pageable) {
        List<ShortUserResponse> followings = userService.getFollowingsPage(userId, pageable)
                .map(userMapper::toShortUserResponse)
                .toList();
        return HttpUtils.ok(Map.of("followings", followings));
    }

    @Operation(summary = "Add currenly logged user to followers of user with id {userId}")
    @PutMapping("/{userId}/followers")
    public ResponseEntity<ResponseBody> addFollower(@PathVariable long userId, @AuthenticationPrincipal Jwt jwt) {
        try {
            userService.addFollower(userId, jwt.getClaim(JwtService.CLAIM_USER_ID));
        } catch (IllegalArgumentException ex) {
            log.warn("Cannot add following: {}", ex.getMessage());
            return HttpUtils.badRequest(ex.getMessage());
        }
        String message = messageSource.getMessage("user.following.added", null,
                LocaleContextHolder.getLocale());
        return HttpUtils.ok(message);
    }

    @Operation(summary = "Delete currently logged in user from followers of user with id {userId}")
    @DeleteMapping("/{userId}/followers")
    public ResponseEntity<ResponseBody> deleteFollower(@PathVariable long userId, @AuthenticationPrincipal Jwt jwt) {
        try {
            userService.removeFollower(userId, jwt.getClaim(JwtService.CLAIM_USER_ID));
        } catch (IllegalArgumentException ex) {
            return HttpUtils.badRequest(ex.getMessage());
        } catch (NoSuchElementException ex) {
            return HttpUtils.notFound(ex.getMessage());
        }
        String message = messageSource.getMessage("user.following.removed", null,
                LocaleContextHolder.getLocale());
        return HttpUtils.ok(message);
    }

    @Operation(summary = "Get user activities(activities user is going through)")
    @GetMapping("/{userId}/activities")
    public ResponseEntity<ResponseBody> getUserActivities(@PathVariable long userId, Pageable pageable) {
        List<ShortUserActivityResponse> userActivities = activityService.getUserActivitiesPage(userId, pageable)
                .map(userActivityMapper::toShortUserActivityResponse)
                .toList();
        return HttpUtils.ok(Map.of("user-activities", userActivities));
    }

    @GetMapping("/{userId}/activities/count")
    public ResponseEntity<ResponseBody> getUserActivityCount(@PathVariable long userId) {
        long count = activityService.getUserActivityCount(userId);
        return HttpUtils.ok(Map.of("user-activity-count", count));
    }

}
