package com.my.selfimprovement.repository.filter;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class EntityPageRequest {
    private int pageNumber = 0;
    private int pageSize = 20;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy;
}
