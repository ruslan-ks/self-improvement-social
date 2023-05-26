package com.my.selfimprovement.dto.request;

import com.my.selfimprovement.util.validation.activity.NewActivityRequestValidator;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

/**
 * Contains data of one of Activity types according to {@code activityType} field.<br>
 * Only fields that belong to the type specified in ActivityType are taken into account when mapping it
 * to an entity
 * @see com.my.selfimprovement.dto.mapper.ActivityMapper
 * @see NewActivityRequestValidator
 */
@Data
public class NewActivityRequest {

    @Size(min = 2, max = 255, message = "{valid.activity.name.size}")
    @NotEmpty(message = "{valid.activity.name.notEmpty}")
    private String name;

    @Size(min = 2, max = 255, message = "{valid.activity.description.size}")
    @NotEmpty(message = "{valid.activity.description.notEmpty}")
    private String description;

    @Min(value = 1, message = "{valid.activity.minutesRequired}")
    @Max(value = 9999, message = "{valid.activity.minutesRequired}")
    private int minutesRequired;

    @NotEmpty(message = "{valid.activity.categoryIds.notEmpty}")
    private Set<Long> categoryIds;

    @NotNull
    private ActivityType type = ActivityType.REGULAR;

    // LimitedCompletionsActivity field
    @Min(value = 1, message = "{valid.limitedCompletionsActivity.completionsLimit}")
    @Max(value = 9999, message = "{valid.limitedCompletionsActivity.completionsLimit}")
    private Integer completionsLimit;

    // PeriodicallyLimitedCompletionsActivity field
    @Min(value = 1, message = "{valid.limitedCompletionsActivity.periodDurationMinutes}")
    @Max(value = 99_999, message = "{valid.limitedCompletionsActivity.periodDurationMinutes}")
    private Long periodDurationMinutes;

}
