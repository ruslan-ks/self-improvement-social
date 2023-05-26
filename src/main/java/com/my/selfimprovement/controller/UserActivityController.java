package com.my.selfimprovement.controller;

import com.my.selfimprovement.dto.mapper.UserActivityMapper;
import com.my.selfimprovement.dto.response.DetailedUserActivityResponse;
import com.my.selfimprovement.dto.response.ResponseBody;
import com.my.selfimprovement.dto.response.ShortUserActivityResponse;
import com.my.selfimprovement.entity.UserActivity;
import com.my.selfimprovement.service.UserActivityService;
import com.my.selfimprovement.service.token.JwtService;
import com.my.selfimprovement.util.HttpUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserActivityController {

    private final UserActivityService userActivityService;

    private final JwtService jwtService;

    private final UserActivityMapper userActivityMapper;

    @Operation(summary = "Get user activities(activities user is going through)")
    @GetMapping("/{userId}/activities")
    public ResponseBody getUserActivities(@PathVariable long userId, Pageable pageable) {
        List<ShortUserActivityResponse> userActivities = userActivityService.getPage(userId, pageable)
                .map(userActivityMapper::toShortUserActivityResponse)
                .toList();
        return ResponseBody.ok("userActivities", userActivities);
    }

    @GetMapping("/{userId}/activities/count")
    public ResponseBody getUserActivityCount(@PathVariable long userId) {
        long count = userActivityService.count(userId);
        return ResponseBody.ok("user-activity-count", count);
    }

    @PostMapping("/activities")
    public ResponseEntity<ResponseBody> addUserActivity(@RequestParam("activityId") long activityId,
                                                        @AuthenticationPrincipal Jwt jwt) {
        try {
            userActivityService.add(activityId, jwtService.getUserId(jwt));
        } catch (IllegalStateException ex) {
            log.warn("Failed to add user activity: " + ex.getMessage());
            return HttpUtils.badRequest(ex.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/activities/{activityId}")
    public void deleteUserActivity(@PathVariable long activityId, @AuthenticationPrincipal Jwt jwt) {
        userActivityService.delete(jwtService.getUserId(jwt), activityId);
    }

    @Operation(summary = "Get user activity details including completions")
    @GetMapping("/{userId}/activities/{activityId}")
    public ResponseBody getUserActivity(@PathVariable long userId, @PathVariable long activityId) {
        UserActivity userActivity = userActivityService.get(userId, activityId);
        DetailedUserActivityResponse response = userActivityMapper.toDetailedUserActivityResponse(userActivity);
        return ResponseBody.ok("userActivity", response);
    }

    @Operation(summary = "Get user activities details including completions")
    @GetMapping("/{userId}/activities/completions")
    public ResponseBody getUserActivitiesCompletions(@PathVariable long userId, Pageable pageable) {
        List<DetailedUserActivityResponse> userActivities = userActivityService.getPage(userId, pageable)
                .map(userActivityMapper::toDetailedUserActivityResponse)
                .toList();
        return ResponseBody.ok("userActivities", userActivities);
    }


}
