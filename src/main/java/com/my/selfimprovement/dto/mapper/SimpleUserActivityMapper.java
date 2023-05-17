package com.my.selfimprovement.dto.mapper;

import com.my.selfimprovement.dto.response.ShortActivityResponse;
import com.my.selfimprovement.dto.response.ShortUserActivityResponse;
import com.my.selfimprovement.entity.UserActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleUserActivityMapper implements UserActivityMapper {

    private final ActivityMapper activityMapper;

    @Override
    public ShortUserActivityResponse toShortUserActivityResponse(UserActivity userActivity) {
        ShortActivityResponse activityResponse = activityMapper.toShortActivityResponse(userActivity.getActivity());
        return ShortUserActivityResponse.builder()
                .activityResponse(activityResponse)
                .startedAt(userActivity.getStartedAt())
                .completionCount(userActivity.getCompletions().size())
                .build();
    }

}
