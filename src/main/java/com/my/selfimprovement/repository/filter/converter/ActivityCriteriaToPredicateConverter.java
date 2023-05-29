package com.my.selfimprovement.repository.filter.converter;

import com.my.selfimprovement.entity.Activity;
import com.my.selfimprovement.repository.filter.FilterCriteria;
import com.my.selfimprovement.repository.filter.FilterOperation;
import com.my.selfimprovement.util.exception.FilterCriteriaException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ActivityCriteriaToPredicateConverter extends CriteriaToPredicateConverterChain<Activity> {

    private static final String CATEGORIES = "categories";

    public ActivityCriteriaToPredicateConverter(
            @Qualifier("genericCriteriaToPredicateConverter") CriteriaToPredicateConverterChain<Activity> converter) {
        super(converter);
    }

    @Override
    public Predicate convert(FilterCriteria criteria, CriteriaBuilder builder, Root<?> root)
            throws FilterCriteriaException {
        if (criteria.operation() == FilterOperation.CONTAINS && criteria.field().equals(CATEGORIES)) {
            return builder.isMember(criteria.value(), root.get(CATEGORIES));
        }
        return callNext(criteria, builder, root);
    }

}
