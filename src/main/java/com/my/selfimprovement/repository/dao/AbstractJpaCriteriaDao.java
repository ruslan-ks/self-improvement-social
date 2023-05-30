package com.my.selfimprovement.repository.dao;

import com.my.selfimprovement.repository.filter.EntityPageRequest;
import com.my.selfimprovement.repository.filter.FilterCriteria;
import com.my.selfimprovement.repository.filter.converter.CriteriaToPredicateConverter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractJpaCriteriaDao<T> implements CriteriaDao<T> {

    protected final Class<T> entityClass;

    protected final CriteriaToPredicateConverter criteriaToPredicateConverter;

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public Page<T> getPage(EntityPageRequest pageRequest, Collection<FilterCriteria> criteriaList) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);

        Predicate predicate = buildPredicate(criteriaList, builder, root);
        criteriaQuery.where(predicate);
        criteriaQuery.orderBy(buildOrder(pageRequest, root, builder));

        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageRequest.getPage() * pageRequest.getSize());
        typedQuery.setMaxResults(pageRequest.getSize());

        Pageable pageable = getPageable(pageRequest);
        long entityCount = getCount(criteriaList, builder);

        return new PageImpl<>(typedQuery.getResultList(), pageable, entityCount);
    }

    protected Predicate buildPredicate(Collection<FilterCriteria> criteriaList, CriteriaBuilder builder,
                                       Root<T> root) {
        List<Predicate> predicateList = criteriaList.stream()
                .map(criteria -> criteriaToPredicateConverter.convert(criteria, builder, root))
                .toList();
        return combine(predicateList);
    }

    protected Order buildOrder(EntityPageRequest pageRequest, Root<T> root, CriteriaBuilder builder) {
        if (pageRequest.getSortDirection().isAscending()) {
            return builder.asc(root.get(pageRequest.getSortBy()));
        }
        return builder.desc(root.get(pageRequest.getSortBy()));
    }

    private Pageable getPageable(EntityPageRequest activityPaging) {
        Sort sort = Sort.by(activityPaging.getSortDirection(), activityPaging.getSortBy());
        return PageRequest.of(activityPaging.getPage(), activityPaging.getSize(), sort);
    }

    private long getCount(Collection<FilterCriteria> criteriaList, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
        Predicate predicate = buildPredicate(criteriaList, criteriaBuilder, root);
        cq.select(criteriaBuilder.count(root));
        cq.where(predicate);
        return entityManager.createQuery(cq).getSingleResult();
    }

    protected abstract Predicate combine(List<Predicate> predicateList);

}
