package rkostiuk.selfimprovement.dto.mapper;

import rkostiuk.selfimprovement.entity.UserActivity;
import rkostiuk.selfimprovement.entity.UserActivityCompletion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rkostiuk.selfimprovement.dto.response.DetailedUserActivityResponse;
import rkostiuk.selfimprovement.dto.response.ShortActivityResponse;
import rkostiuk.selfimprovement.dto.response.ShortUserActivityResponse;

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
