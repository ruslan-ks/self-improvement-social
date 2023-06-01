package rkostiuk.selfimprovement.repository.filter.converter;

import rkostiuk.selfimprovement.repository.filter.FilterCriteria;
import rkostiuk.selfimprovement.util.exception.FilterCriteriaConversionException;
import rkostiuk.selfimprovement.util.exception.FilterCriteriaException;
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
            throw new FilterCriteriaConversionException("Failed to convert criteria. No suitable converter found for "
                    + criteria);
        }
        return fallbackConverter.convert(criteria, builder, root);
    }

}
