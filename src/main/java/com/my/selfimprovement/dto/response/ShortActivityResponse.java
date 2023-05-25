package com.my.selfimprovement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.my.selfimprovement.dto.request.ActivityType;
import lombok.Data;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShortActivityResponse {
    private ActivityType type = ActivityType.REGULAR;

    // Activity properties
    private long id;
    private String name;
    private String description;
    private int minutesRequired;
    private long authorId;
    private Set<Long> categoryIds;
    private long userCount;

    // LimitedCompletionsActivity properties
    private Long completionsLimit;

    // PeriodicalLimitedCompletionsActivity properties
    private long periodDurationMinutes;
}
