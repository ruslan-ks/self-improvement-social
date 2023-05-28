package com.my.selfimprovement.repository.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityPageRequest extends EntityPageRequest {
    private String sortBy = "name";
}
