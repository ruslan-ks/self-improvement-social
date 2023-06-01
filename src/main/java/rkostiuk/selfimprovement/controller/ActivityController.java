package rkostiuk.selfimprovement.controller;

import rkostiuk.selfimprovement.dto.mapper.ActivityMapper;
import rkostiuk.selfimprovement.dto.request.NewActivityRequest;
import rkostiuk.selfimprovement.dto.response.DetailedActivityResponse;
import rkostiuk.selfimprovement.dto.response.ResponseBody;
import rkostiuk.selfimprovement.dto.response.ShortActivityResponse;
import rkostiuk.selfimprovement.entity.Activity;
import rkostiuk.selfimprovement.repository.filter.ActivityPageRequest;
import rkostiuk.selfimprovement.service.ActivityService;
import rkostiuk.selfimprovement.service.token.JwtService;
import rkostiuk.selfimprovement.util.HttpUtils;
import rkostiuk.selfimprovement.util.exception.ActivityNotFoundException;
import rkostiuk.selfimprovement.util.validation.abstracts.ControllerLayerValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<ResponseBody> getPage(ActivityPageRequest pageRequest, Optional<String> query) {
        Page<Activity> page;
        try {
            page = activityService.getPage(pageRequest, query.orElse(""));
        } catch (IllegalArgumentException ex) {
            return HttpUtils.badRequest(ex.getMessage());
        }
        long totalCount = page.getTotalElements();
        long pageNumber = page.getNumber();
        long pageSize = page.getSize();
        List<ShortActivityResponse> dtoList = page.stream()
                .map(activityMapper::toShortActivityResponse)
                .toList();
        Map<String, ?> responseDataMap = Map.of(
                "count", totalCount,
                "page", pageNumber,
                "size", pageSize,
                "activities", dtoList
        );
        return ResponseEntity.ok(ResponseBody.ok(responseDataMap));
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
