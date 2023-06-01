package rkostiuk.selfimprovement.dto.mapper;

import rkostiuk.selfimprovement.dto.response.DetailedUserActivityResponse;
import rkostiuk.selfimprovement.dto.response.ShortUserActivityResponse;
import rkostiuk.selfimprovement.entity.UserActivity;

public interface UserActivityMapper {
    ShortUserActivityResponse toShortUserActivityResponse(UserActivity userActivity);
    DetailedUserActivityResponse toDetailedUserActivityResponse(UserActivity userActivity);
}
