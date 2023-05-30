package com.my.selfimprovement.repository.dao;

import com.my.selfimprovement.repository.filter.converter.CriteriaToPredicateConverter;
import jakarta.persistence.criteria.*;

import java.util.List;

public class ConjunctionJpaCriteriaDao<T> extends AbstractJpaCriteriaDao<T> {

    public ConjunctionJpaCriteriaDao(Class<T> entityClass, CriteriaToPredicateConverter criteriaToPredicateConverter) {
        super(entityClass, criteriaToPredicateConverter);
    }

    @Override
    protected Predicate combine(List<Predicate> predicateList) {
        return entityManager.getCriteriaBuilder().and(predicateList.toArray(new Predicate[0]));
    }

}
