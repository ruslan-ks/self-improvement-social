package rkostiuk.selfimprovement.config;

import rkostiuk.selfimprovement.entity.Activity;
import rkostiuk.selfimprovement.entity.User;
import rkostiuk.selfimprovement.repository.dao.ConjunctionJpaCriteriaDao;
import rkostiuk.selfimprovement.repository.dao.CriteriaDao;
import rkostiuk.selfimprovement.repository.filter.converter.ActivityCriteriaToPredicateConverter;
import rkostiuk.selfimprovement.repository.filter.converter.UserCriteriaToPredicateConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CriteriaDaosConfig {

    @Bean
    public CriteriaDao<Activity> activityCriteriaDao(ActivityCriteriaToPredicateConverter converter) {
        return new ConjunctionJpaCriteriaDao<>(Activity.class, converter);
    }

    @Bean
    public CriteriaDao<User> userCriteriaDao(UserCriteriaToPredicateConverter converter) {
        return new ConjunctionJpaCriteriaDao<>(User.class, converter);
    }

}
