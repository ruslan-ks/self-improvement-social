package com.my.selfimprovement.repository.dao;

import com.my.selfimprovement.repository.filter.EntityPageRequest;
import com.my.selfimprovement.repository.filter.FilterCriteria;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface CriteriaDao <T> {
    Page<T> getPage(EntityPageRequest pageRequest, Collection<FilterCriteria> criteriaList);
}
