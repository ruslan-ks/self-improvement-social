package com.my.selfimprovement.repository.dao;

import com.my.selfimprovement.entity.Activity;
import com.my.selfimprovement.repository.filter.EntityPageRequest;
import com.my.selfimprovement.repository.filter.FilterCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class JpaActivityCriteriaDao implements CriteriaDao<Activity> {

    private final ConjunctionJpaCriteriaDao<Activity> activityConjunctionJpaCriteriaDao;

    @Override
    public Page<Activity> getPage(EntityPageRequest pageRequest, Collection<FilterCriteria> criteriaList) {
        return activityConjunctionJpaCriteriaDao.getPage(pageRequest, criteriaList);
    }

}
