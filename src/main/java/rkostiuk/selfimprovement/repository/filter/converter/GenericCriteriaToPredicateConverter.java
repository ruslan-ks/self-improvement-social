package rkostiuk.selfimprovement.repository.filter.converter;

import rkostiuk.selfimprovement.repository.filter.FilterCriteria;
import rkostiuk.selfimprovement.util.exception.FilterCriteriaConversionException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("genericCriteriaToPredicateConverter")
@Slf4j
public class GenericCriteriaToPredicateConverter extends CriteriaToPredicateConverterChain {

    public GenericCriteriaToPredicateConverter() {
        super(null);
    }

    @Override
    public Predicate convert(FilterCriteria criteria, CriteriaBuilder builder, Root<?> root)
            throws FilterCriteriaConversionException {
        String field = criteria.field();
        Object value = criteria.value();
        try {
            return switch (criteria.operation()) {
                case EQUAL -> builder.equal(root.get(field), value);
                case NOT_EQUAL -> builder.notEqual(root.get(field), value);
                case LIKE -> builder.like(builder.lower(root.get(field)),
                        "%" + value.toString().toLowerCase() + "%");
                case NULL -> builder.isNull(root.get(field));
                case NOT_NULL -> builder.isNotNull(root.get(field));
                case GREATER_EQUAL -> builder.greaterThanOrEqualTo(root.get(field), value.toString());
                case LESS_EQUAL -> builder.lessThanOrEqualTo(root.get(field), value.toString());
                default -> callNext(criteria, builder, root);
            };
        } catch (Exception ex) {
            log.warn("Failed to build predicate", ex);
            throw new FilterCriteriaConversionException("Failed to build predicate for criteria " + criteria, ex);
        }
    }

}
