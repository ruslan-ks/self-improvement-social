package com.my.selfimprovement.dto.mapper;

import com.my.selfimprovement.dto.response.DetailedUserActivityResponse;
import com.my.selfimprovement.dto.response.ShortUserActivityResponse;
import com.my.selfimprovement.entity.UserActivity;

public interface UserActivityMapper {
    ShortUserActivityResponse toShortUserActivityResponse(UserActivity userActivity);
    DetailedUserActivityResponse toDetailedUserActivityResponse(UserActivity userActivity);
}
