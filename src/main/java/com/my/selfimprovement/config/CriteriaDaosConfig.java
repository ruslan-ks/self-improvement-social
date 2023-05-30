package com.my.selfimprovement.config;

import com.my.selfimprovement.entity.Activity;
import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.repository.dao.ConjunctionJpaCriteriaDao;
import com.my.selfimprovement.repository.dao.CriteriaDao;
import com.my.selfimprovement.repository.filter.converter.ActivityCriteriaToPredicateConverter;
import com.my.selfimprovement.repository.filter.converter.GenericCriteriaToPredicateConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CriteriaDaosConfig {

    @Bean
    public CriteriaDao<Activity> activityCriteriaDao(ActivityCriteriaToPredicateConverter converter) {
        return new ConjunctionJpaCriteriaDao<>(Activity.class, converter);
    }

    @Bean
    public CriteriaDao<User> userCriteriaDao(GenericCriteriaToPredicateConverter<User> converter) {
        return new ConjunctionJpaCriteriaDao<>(User.class, converter);
    }

}
