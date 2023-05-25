package com.my.selfimprovement.dto.mapper;

import com.my.selfimprovement.dto.response.*;
import com.my.selfimprovement.entity.UserActivity;
import com.my.selfimprovement.entity.UserActivityCompletion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SimpleUserActivityMapper implements UserActivityMapper {

    private final ActivityMapper activityMapper;

    @Override
    public ShortUserActivityResponse toShortUserActivityResponse(UserActivity userActivity) {
        ShortActivityResponse activityResponse = activityMapper.toShortActivityResponse(userActivity.getActivity());
        return ShortUserActivityResponse.builder()
                .activity(activityResponse)
                .startedAt(userActivity.getStartedAt())
                .completionCount(userActivity.getCompletions().size())
                .build();
    }

    @Override
    public DetailedUserActivityResponse toDetailedUserActivityResponse(UserActivity userActivity) {
        List<Instant> completions = userActivity.getCompletions().stream()
                .map(UserActivityCompletion::getCompletedAt)
                .toList();
        ShortActivityResponse activity = activityMapper.toShortActivityResponse(userActivity.getActivity());
        return DetailedUserActivityResponse.builder()
                .activity(activity)
                .completions(completions)
                .startedAt(userActivity.getStartedAt())
                .build();
    }

}
