package com.my.selfimprovement.repository;

import com.my.selfimprovement.entity.Activity;
import org.springframework.stereotype.Repository;

@Repository
public class JpaActivityCriteriaDao extends ConjunctionJpaCriteriaDao<Activity> implements CriteriaDao<Activity> {

    public JpaActivityCriteriaDao() {
        super(Activity.class);
    }

}
