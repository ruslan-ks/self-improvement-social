package com.my.selfimprovement.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class DetailedUserActivityResponse {
    private ShortActivityResponse activity;
    private Instant startedAt;
    private List<Instant> completions;
}
