package com.my.selfimprovement.repository.filter;

import lombok.Data;

@Data
public class ActivityCriteria {
    private String name;
    private String description;
    private Long authorId;
}
