package com.my.selfimprovement.dto.request;

import jakarta.validation.constraints.*;

import java.util.Set;

public record NewActivityRequest(
        @Size(min = 2, max = 256, message = "{valid.activity.name.size}")
        @NotEmpty(message = "{valid.activity.name.notEmpty}")
        String name,
        @Size(min = 2, max = 256, message = "{valid.activity.description.size}")
        @NotEmpty(message = "{valid.activity.description.notEmpty}")
        String description,
        @Min(value = 1, message = "{valid.activity.minutesRequired}")
        @Max(value = 10000, message = "{valid.activity.minutesRequired}")
        int minutesRequired,
        @NotEmpty(message = "{valid.activity.categoryIds.notEmpty}")
        Set<Long> categoryIds
) {}
