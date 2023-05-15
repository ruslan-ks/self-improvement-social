package com.my.selfimprovement.dto.mapper;

import com.my.selfimprovement.dto.request.ActivityType;
import com.my.selfimprovement.dto.request.NewActivityRequest;
import com.my.selfimprovement.dto.response.DetailedActivityResponse;
import com.my.selfimprovement.entity.Activity;
import com.my.selfimprovement.entity.Category;
import com.my.selfimprovement.entity.LimitedCompletionsActivity;
import com.my.selfimprovement.entity.PeriodicalLimitedCompletionsActivity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ActivityModelMapper implements ActivityMapper {

    private final ModelMapper modelMapper;

    @Override
    public Activity toActivity(NewActivityRequest newActivityRequest) {
        return switch (newActivityRequest.getActivityType()) {
            case REGULAR -> modelMapper.map(newActivityRequest, Activity.class);
            case LIMITED_COMPLETIONS -> modelMapper.map(newActivityRequest, LimitedCompletionsActivity.class);
            case PERIODICAL_LIMITED_COMPLETIONS ->
                    modelMapper.map(newActivityRequest, PeriodicalLimitedCompletionsActivity.class);
        };
    }

    @Override
    public DetailedActivityResponse toDetailedActivityResponse(Activity activity) {
        DetailedActivityResponse response = modelMapper.map(activity, DetailedActivityResponse.class);
        Set<Long> categoryIds = activity.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        response.setCategoryIds(categoryIds);
        Set<Long> userIds = activity.getUserActivities().stream()
                .map(ua -> ua.getUser().getId())
                .collect(Collectors.toSet());
        response.setUserIds(userIds);
        ActivityType type = typeFor(activity);
        response.setActivityType(type);
        return response;
    }

    private ActivityType typeFor(Activity activity) {
        if (activity instanceof PeriodicalLimitedCompletionsActivity) {
            return ActivityType.PERIODICAL_LIMITED_COMPLETIONS;
        }
        if (activity instanceof LimitedCompletionsActivity) {
            return ActivityType.LIMITED_COMPLETIONS;
        }
        return ActivityType.REGULAR;
    }

}
