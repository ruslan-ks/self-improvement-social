package com.my.selfimprovement.repository.dao;

import com.my.selfimprovement.entity.Activity;
import com.my.selfimprovement.repository.filter.converter.ActivityCriteriaToPredicateConverter;
import org.springframework.stereotype.Repository;

@Repository
public class JpaActivityCriteriaDao extends ConjunctionJpaCriteriaDao<Activity> implements CriteriaDao<Activity> {

    public JpaActivityCriteriaDao(ActivityCriteriaToPredicateConverter converter) {
        super(Activity.class, converter);
    }

}
