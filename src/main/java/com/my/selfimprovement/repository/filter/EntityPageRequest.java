package com.my.selfimprovement.repository.filter;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class EntityPageRequest {
    private int page = 0;
    private int size = 20;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy;
}
