package rkostiuk.selfimprovement.controller;

import rkostiuk.selfimprovement.dto.mapper.UserActivityMapper;
import rkostiuk.selfimprovement.dto.response.DetailedUserActivityResponse;
import rkostiuk.selfimprovement.dto.response.ResponseBody;
import rkostiuk.selfimprovement.dto.response.ShortUserActivityResponse;
import rkostiuk.selfimprovement.entity.UserActivity;
import rkostiuk.selfimprovement.service.UserActivityService;
import rkostiuk.selfimprovement.service.token.JwtService;
import rkostiuk.selfimprovement.util.HttpUtils;
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
    public rkostiuk.selfimprovement.dto.response.ResponseBody getUserActivities(@PathVariable long userId, Pageable pageable) {
        List<ShortUserActivityResponse> userActivities = userActivityService.getPage(userId, pageable)
                .map(userActivityMapper::toShortUserActivityResponse)
                .toList();
        return rkostiuk.selfimprovement.dto.response.ResponseBody.ok("userActivities", userActivities);
    }

    @GetMapping("/{userId}/activities/count")
    public rkostiuk.selfimprovement.dto.response.ResponseBody getUserActivityCount(@PathVariable long userId) {
        long count = userActivityService.count(userId);
        return rkostiuk.selfimprovement.dto.response.ResponseBody.ok("userActivityCount", count);
    }

    @PostMapping("/activities")
    public ResponseEntity<rkostiuk.selfimprovement.dto.response.ResponseBody> addUserActivity(@RequestParam("activityId") long activityId,
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
    public rkostiuk.selfimprovement.dto.response.ResponseBody getUserActivity(@PathVariable long userId, @PathVariable long activityId) {
        UserActivity userActivity = userActivityService.getByKeyOrElseThrow(userId, activityId);
        DetailedUserActivityResponse response = userActivityMapper.toDetailedUserActivityResponse(userActivity);
        return rkostiuk.selfimprovement.dto.response.ResponseBody.ok("userActivity", response);
    }

    @Operation(summary = "Get user activities details including completions")
    @GetMapping("/{userId}/activities/completions")
    public rkostiuk.selfimprovement.dto.response.ResponseBody getUserActivitiesCompletions(@PathVariable long userId, Pageable pageable) {
        List<DetailedUserActivityResponse> userActivities = userActivityService.getPage(userId, pageable)
                .map(userActivityMapper::toDetailedUserActivityResponse)
                .toList();
        return rkostiuk.selfimprovement.dto.response.ResponseBody.ok("userActivities", userActivities);
    }

    @Operation(summary = "Add user activity completion")
    @PostMapping("/activities/{activityId}/completions")
    public ResponseEntity<ResponseBody> addUserActivityCompletion(@PathVariable long activityId, @AuthenticationPrincipal Jwt jwt) {
        long userId = jwtService.getUserId(jwt);
        try {
            userActivityService.addCompletion(userId, activityId);
        } catch (IllegalStateException ex) {
            return HttpUtils.badRequest(ex.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
