package com.my.selfimprovement.dto.request;

import com.my.selfimprovement.entity.RepetitiveActivity;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

/**
 * Contains data of one of Activity types according to {@code activityType} field.<br>
 * Only fields that belong to the type specified in ActivityType are taken into account when mapping it
 * to an entity(see {@link com.my.selfimprovement.dto.mapper.ActivityMapper})
 */
@Data
public class NewActivityRequest {

    @Size(min = 2, max = 256, message = "{valid.activity.name.size}")
    @NotEmpty(message = "{valid.activity.name.notEmpty}")
    private String name;

    @Size(min = 2, max = 256, message = "{valid.activity.description.size}")
    @NotEmpty(message = "{valid.activity.description.notEmpty}")
    private String description;

    @Min(value = 1, message = "{valid.activity.minutesRequired}")
    @Max(value = 10000, message = "{valid.activity.minutesRequired}")
    private int minutesRequired;

    @NotEmpty(message = "{valid.activity.categoryIds.notEmpty}")
    private Set<Long> categoryIds;

    @NotNull
    private ActivityType activityType = ActivityType.REGULAR;

    @NotNull(message = "{valid.repetitiveActivity.periodType.notNull}")
    private RepetitiveActivity.PeriodType periodType = RepetitiveActivity.PeriodType.NO_PERIOD;

    @Min(value = 0, message = "{valid.repetitiveActivity.timesPerPeriod}")
    @Max(value = 10000, message = "{valid.repetitiveActivity.timesPerPeriod}")
    private int timesPerPeriod;

}
