package com.my.selfimprovement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.my.selfimprovement.dto.request.ActivityType;

import com.my.selfimprovement.entity.PeriodicalLimitedCompletionsActivity;
import lombok.Data;

import java.util.Set;

/**
 * Represents response for any activity type. Only fields belonging to specified {@code activityType}
 * may be set, other fields must be null
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailedActivityResponse {
    private ActivityType activityType = ActivityType.REGULAR;

    // Activity properties
    private long id;
    private String name;
    private String description;
    private int minutesRequired;
    private long authorId;
    private Set<Long> categoryIds;
    private Set<Long> userIds;

    // LimitedCompletionsActivity properties
    private Long completionsLimit;

    // PeriodicalLimitedCompletionsActivity properties
    private PeriodicalLimitedCompletionsActivity.PeriodType periodType;
}
