package com.my.selfimprovement.repository.filter.converter;

import com.my.selfimprovement.repository.filter.FilterCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

@Component("genericCriteriaToPredicateConverter")
public class GenericCriteriaToPredicateConverter<T> extends CriteriaToPredicateConverterChain<T> {

    public GenericCriteriaToPredicateConverter() {
        super(null);
    }

    @Override
    public Predicate convert(FilterCriteria criteria, CriteriaBuilder builder, Root<?> root) {
        String field = criteria.field();
        Object value = criteria.value();
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
    }

}
