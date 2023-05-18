package com.my.selfimprovement.controller;

import com.my.selfimprovement.dto.mapper.ActivityMapper;
import com.my.selfimprovement.dto.request.NewActivityRequest;
import com.my.selfimprovement.dto.response.DetailedActivityResponse;
import com.my.selfimprovement.dto.response.ResponseBody;
import com.my.selfimprovement.dto.response.ShortActivityResponse;
import com.my.selfimprovement.entity.Activity;
import com.my.selfimprovement.service.ActivityService;
import com.my.selfimprovement.service.token.JwtService;
import com.my.selfimprovement.util.HttpUtils;
import com.my.selfimprovement.util.exception.ActivityNotFoundException;
import com.my.selfimprovement.util.validation.abstracts.ControllerLayerValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
@Slf4j
public class ActivitiesController {

    private final ActivityMapper activityMapper;

    private final ActivityService activityService;

    private final JwtService jwtService;

    private final ControllerLayerValidator<NewActivityRequest> newActivityRequestValidator;

    @PostMapping
    public ResponseEntity<ResponseBody> create(@RequestBody @Valid NewActivityRequest request,
                                               @AuthenticationPrincipal Jwt jwt) {
        newActivityRequestValidator.validate(request);
        long authorId = jwtService.getUserId(jwt);
        Activity createdActivity = activityService.create(request, authorId);
        DetailedActivityResponse response = activityMapper.toDetailedActivityResponse(createdActivity);
        return HttpUtils.ok(Map.of("activity", response));
    }

    @GetMapping
    public ResponseEntity<ResponseBody> getPage(Pageable pageable) {
        List<ShortActivityResponse> activities = activityService.getPage(pageable)
                .map(activityMapper::toShortActivityResponse)
                .toList();
        return HttpUtils.ok(Map.of("activities", activities));
    }

    @GetMapping("/count")
    public ResponseEntity<ResponseBody> getCount() {
        long count = activityService.count();
        return HttpUtils.ok(Map.of("activityCount", count));
    }

    @GetMapping("{activityId}")
    public ResponseEntity<ResponseBody> getById(@PathVariable long activityId) {
        return activityService.getById(activityId)
                .map(activityMapper::toDetailedActivityResponse)
                .map(dto -> Map.of("activity", dto))
                .map(HttpUtils::ok)
                .orElseThrow(() -> new ActivityNotFoundException("Activity with id " + activityId + " not found"));
    }

}
