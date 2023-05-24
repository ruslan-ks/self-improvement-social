package com.my.selfimprovement.dto.mapper;

import com.my.selfimprovement.dto.request.NewActivityRequest;
import com.my.selfimprovement.dto.response.DetailedActivityResponse;
import com.my.selfimprovement.dto.response.ShortActivityResponse;
import com.my.selfimprovement.entity.Activity;

public interface ActivityMapper {

    /**
     * Maps {@code newActivityRequest} to one of existing activity types depending on
     * {@link NewActivityRequest#getType()}. When mapping, only fields required for particular activity
     * type are taken into account.<br>
     * Dos not map fields that require database requests(for example: author id -> author entity).
     * @param newActivityRequest request to be mapped
     * @return Activity entity object
     */
    Activity toActivity(NewActivityRequest newActivityRequest);

    DetailedActivityResponse toDetailedActivityResponse(Activity activity);

    ShortActivityResponse toShortActivityResponse(Activity activity);

}
