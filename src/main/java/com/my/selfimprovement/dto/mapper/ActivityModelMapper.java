package com.my.selfimprovement.dto.mapper;

import com.my.selfimprovement.dto.request.ActivityType;
import com.my.selfimprovement.dto.request.NewActivityRequest;
import com.my.selfimprovement.dto.response.DetailedActivityResponse;
import com.my.selfimprovement.entity.Activity;
import com.my.selfimprovement.entity.Category;
import com.my.selfimprovement.entity.RepetitiveActivity;
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
            case REPETITIVE -> modelMapper.map(newActivityRequest, RepetitiveActivity.class);
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
        ActivityType type = activity.isRepetitive() ? ActivityType.REPETITIVE : ActivityType.REGULAR;
        response.setActivityType(type);
        return response;
    }

}
