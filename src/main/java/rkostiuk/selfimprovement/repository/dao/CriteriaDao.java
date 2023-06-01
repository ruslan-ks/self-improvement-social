package rkostiuk.selfimprovement.repository.dao;

import rkostiuk.selfimprovement.repository.filter.EntityPageRequest;
import rkostiuk.selfimprovement.repository.filter.FilterCriteria;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface CriteriaDao <T> {
    Page<T> getPage(EntityPageRequest pageRequest, Collection<FilterCriteria> criteriaList);
}
