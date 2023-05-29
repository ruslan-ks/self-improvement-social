package com.my.selfimprovement.repository;

import com.my.selfimprovement.entity.Activity;
import com.my.selfimprovement.repository.filter.FilterCriteria;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

@Repository
public class JpaActivityCriteriaDao extends ConjunctionJpaCriteriaDao<Activity> implements CriteriaDao<Activity> {

    public JpaActivityCriteriaDao() {
        super(Activity.class);
    }

    @Override
    protected Predicate buildContainsOperationPredicate(FilterCriteria criteria, CriteriaBuilder builder,
                                                        Root<Activity> root) {
        if (!criteria.field().equals("categories")) {
            throw new UnsupportedOperationException("Operation " + criteria.operation() + " on field '" +
                    criteria.field() + "' not supported");
        }
        return builder.isMember(criteria.value(), root.get("categories"));
    }

}
