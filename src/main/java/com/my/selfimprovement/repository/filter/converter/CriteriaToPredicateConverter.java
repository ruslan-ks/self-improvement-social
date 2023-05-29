package com.my.selfimprovement.repository.filter.converter;

import com.my.selfimprovement.repository.filter.FilterCriteria;
import com.my.selfimprovement.util.exception.FilterCriteriaException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface CriteriaToPredicateConverter {
    /**
     * Convert filterCriteria into Predicate object
     * @param filterCriteria criteria to be converted
     * @param builder CriteriaBuilder to be used when converting
     * @param root Root object to be used when converting
     * @return Predicate created from criteria
     */
    Predicate convert(FilterCriteria filterCriteria, CriteriaBuilder builder, Root<?> root)
            throws FilterCriteriaException;
}
