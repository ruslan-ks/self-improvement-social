package rkostiuk.selfimprovement.controller;

import rkostiuk.selfimprovement.dto.mapper.UserMapper;
import rkostiuk.selfimprovement.dto.request.UserUpdateRequest;
import rkostiuk.selfimprovement.dto.request.UserRegistrationRequest;
import rkostiuk.selfimprovement.dto.response.DetailedUserResponse;
import rkostiuk.selfimprovement.dto.response.ResponseBody;
import rkostiuk.selfimprovement.dto.response.ShortUserResponse;
import rkostiuk.selfimprovement.entity.User;
import rkostiuk.selfimprovement.repository.filter.UserPageRequest;
import rkostiuk.selfimprovement.service.UserService;
import rkostiuk.selfimprovement.service.token.JwtService;
import rkostiuk.selfimprovement.util.HttpUtils;
import rkostiuk.selfimprovement.util.exception.AvatarNotFoundException;
import rkostiuk.selfimprovement.util.i18n.Translator;
import rkostiuk.selfimprovement.util.i18n.UIMessage;
import rkostiuk.selfimprovement.util.validation.UserRegistrationRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
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
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    private final PasswordEncoder encoder;

    private final UserRegistrationRequestValidator userRegistrationRequestValidator;

    private final UserMapper userMapper;

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
    public ResponseEntity<ResponseBody> getPage(UserPageRequest pageRequest, Optional<String> query) {
        Page<User> page;
        try {
            page = userService.getPage(pageRequest, query.orElse(""));
        } catch (IllegalArgumentException ex) {
            return HttpUtils.badRequest(ex.getMessage());
        }
        return HttpUtils.page("users", page.map(userMapper::toShortUserResponse));
    }

    @GetMapping("/count")
    public ResponseBody count() {
        long count = userService.count();
        return ResponseBody.ok("count", count);
    }

    @GetMapping("{id}")
    public ResponseBody getById(@PathVariable("id") long userId) {
        User user = userService.getByIdOrElseThrow(userId);
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

    @Operation(summary = "Add currently logged user to followers of user with id {userId}")
    @PostMapping("/{userId}/followers")
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

}
