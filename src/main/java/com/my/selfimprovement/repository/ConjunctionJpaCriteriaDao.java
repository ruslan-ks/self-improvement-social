package com.my.selfimprovement.repository;

import com.my.selfimprovement.repository.filter.EntityPageRequest;
import com.my.selfimprovement.repository.filter.FilterCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConjunctionJpaCriteriaDao<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> entityClass;

    protected ConjunctionJpaCriteriaDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Page<T> getPage(EntityPageRequest pageRequest, Collection<FilterCriteria> criteriaList) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);

        Predicate predicate = buildPredicate(criteriaList, builder, root);
        criteriaQuery.where(predicate);
        criteriaQuery.orderBy(buildOrder(pageRequest, root, builder));

        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize());
        typedQuery.setMaxResults(pageRequest.getPageSize());

        //Since Hibernate 6 Predicate cannot be reused. Build the same predicate again

        Pageable pageable = getPageable(pageRequest);
        long entityCount = getCount(criteriaList, builder);

        return new PageImpl<>(typedQuery.getResultList(), pageable, entityCount);
    }

    protected Predicate buildPredicate(Collection<FilterCriteria> criteriaList, CriteriaBuilder builder,
                                     Root<T> root) {
        List<Predicate> predicateList = new ArrayList<>();
        for (var criteria : criteriaList) {
            predicateList.add(filterCriteriaToPredicate(criteria, builder, root));
        }
        return builder.and(predicateList.toArray(new Predicate[0]));
    }

    protected Predicate filterCriteriaToPredicate(FilterCriteria criteria, CriteriaBuilder builder, Root<T> root) {
        String field = criteria.field();
        Object value = criteria.value();
        return switch (criteria.operation()) {
            case EQUAL -> builder.equal(root.get(field), value);
            case NOT_EQUAL -> builder.notEqual(root.get(field), value);
            case LIKE -> builder.like(root.get(field), "%" + value + "%");
//                case CONTAINS -> builder.;
//                case DOES_NOT_CONTAIN -> "";
            case NULL -> builder.isNull(root.get(field));
            case NOT_NULL -> builder.isNotNull(root.get(field));
            case GREATER_EQUAL -> builder.greaterThanOrEqualTo(root.get(field), value.toString());
            case LESS_EQUAL -> builder.lessThanOrEqualTo(root.get(field), value.toString());
            default -> throw new UnsupportedOperationException("Operation '" + criteria.operation() +
                    "' not supported for Activity");
        };
    }

    protected Order buildOrder(EntityPageRequest pageRequest, Root<T> root, CriteriaBuilder builder) {
        if (pageRequest.getSortDirection().isAscending()) {
            return builder.asc(root.get(pageRequest.getSortBy()));
        }
        return builder.desc(root.get(pageRequest.getSortBy()));
    }

    private Pageable getPageable(EntityPageRequest activityPaging) {
        Sort sort = Sort.by(activityPaging.getSortDirection(), activityPaging.getSortBy());
        return PageRequest.of(activityPaging.getPageNumber(), activityPaging.getPageSize(), sort);
    }

    private long getCount(Collection<FilterCriteria> criteriaList, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
        Predicate predicate = buildPredicate(criteriaList, criteriaBuilder, root);
        cq.select(criteriaBuilder.count(root));
        cq.where(predicate);
        return entityManager.createQuery(cq).getSingleResult();
    }

    // TODO: add public count(List<FilterCriteria> criteriaList) method

}
