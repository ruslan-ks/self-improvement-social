package com.my.selfimprovement.repository.filter.converter;

import com.my.selfimprovement.repository.filter.FilterCriteria;
import com.my.selfimprovement.util.exception.FilterCriteriaException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class CriteriaToPredicateConverterChain<T> implements CriteriaToPredicateConverter {

    @Getter
    @Setter
    protected CriteriaToPredicateConverterChain<T> fallbackConverter;

    protected Predicate callNext(FilterCriteria criteria, CriteriaBuilder builder, Root<?> root)
            throws FilterCriteriaException {
        if (fallbackConverter == null) {
            throw new FilterCriteriaException("Failed to convert criteria " + criteria);
        }
        return fallbackConverter.convert(criteria, builder, root);
    }

}
