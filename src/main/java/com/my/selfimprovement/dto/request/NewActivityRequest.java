package com.my.selfimprovement.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record NewActivityRequest(
        @Size(min = 2, max = 256, message = "{valid.activity.name.size}")
        String name,
        @Size(min = 2, max = 256, message = "{valid.activity.description.size}")
        String description,
        @Min(value = 1, message = "{valid.activity.minutesRequired}")
        @Max(value = 10000, message = "{valid.activity.minutesRequired}")
        int minutesRequired,
        Set<Integer> categoryIds
) {}
