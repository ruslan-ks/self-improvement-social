package com.my.selfimprovement.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ShortUserActivityResponse {
    private Instant startedAt;
    private ShortActivityResponse activityResponse;
    private long completionCount;
}
