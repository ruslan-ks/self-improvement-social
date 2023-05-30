package com.my.selfimprovement.repository.filter.converter;

import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.repository.filter.FilterCriteria;
import com.my.selfimprovement.repository.filter.FilterOperation;
import com.my.selfimprovement.util.exception.FilterCriteriaConversionException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

@Component
public class UserCriteriaToPredicateConverter extends CriteriaToPredicateConverterChain<User> {

    public UserCriteriaToPredicateConverter(CriteriaToPredicateConverterChain<User> fallbackConverter) {
        super(fallbackConverter);
    }

    @Override
    public Predicate convert(FilterCriteria criteria, CriteriaBuilder builder, Root<?> root)
            throws FilterCriteriaConversionException {
        // If LIKE operation is applied either to name or surname, both name and surname are checked using or operator:
        // ((name like value) or (surname like value))
        String field = criteria.field();
        if (criteria.operation().equals(FilterOperation.LIKE) && field.equals("name")) {
            String lowerValue = criteria.value().toString().toLowerCase();
            var namePredicate = builder.like(builder.lower(root.get("name")), "%" + lowerValue + "%");
            var surnamePredicate = builder.like(builder.lower(root.get("surname")), "%" + lowerValue + "%");
            return builder.or(namePredicate, surnamePredicate);
        }
        return callNext(criteria, builder, root);
    }

}
