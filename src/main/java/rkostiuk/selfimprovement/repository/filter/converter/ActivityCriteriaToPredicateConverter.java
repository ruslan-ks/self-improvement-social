package rkostiuk.selfimprovement.repository.filter.converter;

import rkostiuk.selfimprovement.repository.filter.FilterCriteria;
import rkostiuk.selfimprovement.repository.filter.FilterOperator;
import rkostiuk.selfimprovement.util.exception.FilterCriteriaConversionException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ActivityCriteriaToPredicateConverter extends CriteriaToPredicateConverterChain {

    private static final String CATEGORIES = "categories";

    public ActivityCriteriaToPredicateConverter(
            @Qualifier("genericCriteriaToPredicateConverter") CriteriaToPredicateConverterChain converter) {
        super(converter);
    }

    @Override
    public Predicate convert(FilterCriteria criteria, CriteriaBuilder builder, Root<?> root)
            throws FilterCriteriaConversionException {
        if (criteria.operation() == FilterOperator.CONTAINS && criteria.field().equals(CATEGORIES)) {
            return builder.isMember(criteria.value(), root.get(CATEGORIES));
        }
        return callNext(criteria, builder, root);
    }

}
