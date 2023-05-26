package com.my.selfimprovement.controller;

import com.my.selfimprovement.dto.mapper.ActivityMapper;
import com.my.selfimprovement.dto.request.NewActivityRequest;
import com.my.selfimprovement.dto.response.DetailedActivityResponse;
import com.my.selfimprovement.dto.response.ResponseBody;
import com.my.selfimprovement.dto.response.ShortActivityResponse;
import com.my.selfimprovement.entity.Activity;
import com.my.selfimprovement.service.ActivityService;
import com.my.selfimprovement.service.token.JwtService;
import com.my.selfimprovement.util.exception.ActivityNotFoundException;
import com.my.selfimprovement.util.validation.abstracts.ControllerLayerValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
@Slf4j
public class ActivityController {

    private final ActivityMapper activityMapper;

    private final ActivityService activityService;

    private final JwtService jwtService;

    private final ControllerLayerValidator<NewActivityRequest> newActivityRequestValidator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseBody create(@RequestBody @Valid NewActivityRequest request,
                                               @AuthenticationPrincipal Jwt jwt) {
        newActivityRequestValidator.validate(request);
        long authorId = jwtService.getUserId(jwt);
        Activity createdActivity = activityService.create(request, authorId);
        DetailedActivityResponse response = activityMapper.toDetailedActivityResponse(createdActivity);
        return ResponseBody.ok("activity", response);
    }

    @GetMapping
    public ResponseBody getPage(Pageable pageable) {
        List<ShortActivityResponse> activities = activityService.getPage(pageable)
                .map(activityMapper::toShortActivityResponse)
                .toList();
        return ResponseBody.ok("activities", activities);
    }

    @GetMapping("/count")
    public ResponseBody getCount() {
        long count = activityService.count();
        return ResponseBody.ok("activityCount", count);
    }

    @GetMapping("{activityId}")
    public ResponseBody getById(@PathVariable long activityId) {
        return activityService.getById(activityId)
                .map(activityMapper::toDetailedActivityResponse)
                .map(dto -> ResponseBody.ok("activity", dto))
                .orElseThrow(() -> new ActivityNotFoundException("Activity with id " + activityId + " not found"));
    }

}
