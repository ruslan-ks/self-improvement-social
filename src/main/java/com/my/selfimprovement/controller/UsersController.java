package com.my.selfimprovement.controller;

import com.my.selfimprovement.dto.mapper.UserActivityMapper;
import com.my.selfimprovement.dto.mapper.UserMapper;
import com.my.selfimprovement.dto.request.UserUpdateRequest;
import com.my.selfimprovement.dto.response.*;
import com.my.selfimprovement.dto.request.UserRegistrationRequest;
import com.my.selfimprovement.dto.response.ResponseBody;
import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.entity.UserActivity;
import com.my.selfimprovement.service.ActivityService;
import com.my.selfimprovement.service.UserService;
import com.my.selfimprovement.service.token.JwtService;
import com.my.selfimprovement.util.HttpUtils;
import com.my.selfimprovement.util.exception.AvatarNotFoundException;
import com.my.selfimprovement.util.i18n.Translator;
import com.my.selfimprovement.util.i18n.UIMessage;
import com.my.selfimprovement.util.validation.UserRegistrationRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final UserService userService;

    private final ActivityService activityService;

    private final JwtService jwtService;

    private final PasswordEncoder encoder;

    private final UserRegistrationRequestValidator userRegistrationRequestValidator;

    private final UserMapper userMapper;

    private final UserActivityMapper userActivityMapper;

    private final Translator translator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseBody create(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {
        userRegistrationRequestValidator.validate(userRegistrationRequest);
        User user = userMapper.toUser(userRegistrationRequest);
        user.setPassword(encoder.encode(user.getPassword()));
        userService.save(user);

        DetailedUserResponse detailedUserResponse = userMapper.toDetailedUserResponse(user);
        return ResponseBody.of(HttpStatus.CREATED, "user", detailedUserResponse);
    }

    @GetMapping
    public ResponseBody getActiveUsersPage(Pageable pageable) {
        List<ShortUserResponse> users = userService.findActiveUsersPage(pageable)
                .map(userMapper::toShortUserResponse)
                .toList();
        return ResponseBody.ok("users", users);
    }

    @GetMapping("/count")
    public ResponseBody count() {
        long count = userService.count();
        return ResponseBody.ok("count", count);
    }

    @GetMapping("{id}")
    public ResponseBody getById(@PathVariable("id") long userId) {
        User user = userService.findByIdOrElseThrow(userId);
        DetailedUserResponse userDto = userMapper.toDetailedUserResponse(user);
        return ResponseBody.ok("user", userDto);
    }

    @PatchMapping
    public ResponseBody update(@RequestBody @Valid UserUpdateRequest userUpdateRequest,
                                               @AuthenticationPrincipal Jwt jwt) {
        long userId = jwt.getClaim(JwtService.CLAIM_USER_ID);
        User updatedUser = userService.update(userId, userUpdateRequest);
        DetailedUserResponse updatedUserResponse = userMapper.toDetailedUserResponse(updatedUser);
        return ResponseBody.ok("user", updatedUserResponse);
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
            UIMessage uiMessage = translator.translateUIMessage("user.avatar.notAttached");
            return HttpUtils.badRequest(uiMessage);
        }
        long userId = jwtService.getUserId(jwt);
        userService.setAvatar(file, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/avatar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAvatar(@AuthenticationPrincipal Jwt jwt) {
        long userId = jwtService.getUserId(jwt);
        userService.removeAvatar(userId);
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseBody getFollowersCount(@PathVariable long userId) {
        long count = userService.getFollowersCount(userId);
        return ResponseBody.ok("followersCount", count);
    }

    @GetMapping("/{userId}/followers")
    public ResponseBody getUserFollowersPage(@PathVariable long userId, Pageable pageable) {
        List<ShortUserResponse> followers = userService.getFollowersPage(userId, pageable)
                .map(userMapper::toShortUserResponse)
                .toList();
        return ResponseBody.ok("followers", followers);
    }

    @GetMapping("/{userId}/followings/count")
    public ResponseBody getFollowingsCount(@PathVariable long userId) {
        long count = userService.getFollowingsCount(userId);
        return ResponseBody.ok("followingsCount", count);
    }


    @GetMapping("/{userId}/followings")
    public ResponseBody getFollowingsPage(@PathVariable long userId, Pageable pageable) {
        List<ShortUserResponse> followings = userService.getFollowingsPage(userId, pageable)
                .map(userMapper::toShortUserResponse)
                .toList();
        return ResponseBody.ok("followings", followings);
    }

    @Operation(summary = "Add currenly logged user to followers of user with id {userId}")
    @PutMapping("/{userId}/followers")
    public ResponseEntity<ResponseBody> addFollower(@PathVariable long userId, @AuthenticationPrincipal Jwt jwt) {
        try {
            userService.addFollower(userId, jwtService.getUserId(jwt));
        } catch (IllegalArgumentException ex) {
            log.warn("Cannot add following: {}", ex.getMessage());
            return HttpUtils.badRequest(ex.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete currently logged in user from followers of user with id {userId}")
    @DeleteMapping("/{userId}/followers")
    public ResponseEntity<ResponseBody> deleteFollower(@PathVariable long userId, @AuthenticationPrincipal Jwt jwt) {
        try {
            userService.removeFollower(userId, jwtService.getUserId(jwt));
        } catch (IllegalArgumentException ex) {
            return HttpUtils.badRequest(ex.getMessage());
        } catch (NoSuchElementException ex) {
            return HttpUtils.notFound(ex.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get user activities(activities user is going through)")
    @GetMapping("/{userId}/activities")
    public ResponseBody getUserActivities(@PathVariable long userId, Pageable pageable) {
        List<ShortUserActivityResponse> userActivities = activityService.getUserActivitiesPage(userId, pageable)
                .map(userActivityMapper::toShortUserActivityResponse)
                .toList();
        return ResponseBody.ok("userActivities", userActivities);
    }

    @GetMapping("/{userId}/activities/count")
    public ResponseBody getUserActivityCount(@PathVariable long userId) {
        long count = activityService.getUserActivityCount(userId);
        return ResponseBody.ok("user-activity-count", count);
    }

    @PostMapping("/activities")
    public ResponseEntity<ResponseBody> addUserActivity(@RequestParam("activityId") long activityId,
                                                        @AuthenticationPrincipal Jwt jwt) {
        try {
            activityService.addUserActivity(activityId, jwtService.getUserId(jwt));
        } catch (IllegalStateException ex) {
            log.warn("Failed to add user activity: " + ex.getMessage());
            return HttpUtils.badRequest(ex.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get user activity details including completions")
    @GetMapping("/{userId}/activities/{activityId}")
    public ResponseBody getUserActivity(@PathVariable long userId, @PathVariable long activityId) {
        UserActivity userActivity = activityService.getUserActivity(userId, activityId);
        DetailedUserActivityResponse response = userActivityMapper.toDetailedUserActivityResponse(userActivity);
        return ResponseBody.ok("userActivity", response);
    }

    @Operation(summary = "Get user activities details including completions")
    @GetMapping("/{userId}/activities/completions")
    public ResponseBody getUserActivitiesCompletions(@PathVariable long userId, Pageable pageable) {
        List<DetailedUserActivityResponse> userActivities = activityService.getUserActivitiesPage(userId, pageable)
                .map(userActivityMapper::toDetailedUserActivityResponse)
                .toList();
        return ResponseBody.ok("userActivities", userActivities);
    }

}
