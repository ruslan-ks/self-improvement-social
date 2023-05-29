package com.my.selfimprovement.repository;

import com.my.selfimprovement.entity.Activity;
import com.my.selfimprovement.repository.filter.ActivityCriteria;
import com.my.selfimprovement.repository.filter.ActivityPageRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ActivityCriteriaRepository {

    private final EntityManager entityManager;

    private final CriteriaBuilder criteriaBuilder;

    public ActivityCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Activity> findPageWithFilters(ActivityPageRequest activityPaging, ActivityCriteria activityCriteria) {
        CriteriaQuery<Activity> criteriaQuery = criteriaBuilder.createQuery(Activity.class);
        Root<Activity> activityRoot = criteriaQuery.from(Activity.class);
        Predicate predicate = getPredicate(activityCriteria, activityRoot);
        criteriaQuery.where(predicate);
        setOrder(activityPaging, criteriaQuery, activityRoot);

        TypedQuery<Activity> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(activityPaging.getPageNumber() * activityPaging.getPageSize());
        typedQuery.setMaxResults(activityPaging.getPageSize());

        Pageable pageable = getPageable(activityPaging);
        long activityCount = getActivityCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, activityCount);
    }

    private Predicate getPredicate(ActivityCriteria activityCriteria, Root<Activity> activityRoot) {
        List<Predicate> predicateList = new ArrayList<>();
        if (Objects.nonNull(activityCriteria.getName())) {
            predicateList.add(criteriaBuilder.like(activityRoot.get("name"),
                    "%" + activityCriteria.getName() + "%"));
        }
        if (Objects.nonNull(activityCriteria.getDescription())) {
            predicateList.add(criteriaBuilder.like(activityRoot.get("description"),
                    "%" + activityCriteria.getDescription() + "%"));
        }
        if (Objects.nonNull(activityCriteria.getAuthorId())) {
            predicateList.add(criteriaBuilder.equal(activityRoot.get("authorId"), activityCriteria.getAuthorId()));
        }
        return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
    }

    private void setOrder(ActivityPageRequest paging, CriteriaQuery<Activity> criteriaQuery, Root<Activity> activityRoot) {
        if (paging.getSortDirection().isAscending()) {
            criteriaQuery.orderBy(criteriaBuilder.asc(activityRoot.get(paging.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(activityRoot.get(paging.getSortBy())));
        }
    }

    private Pageable getPageable(ActivityPageRequest activityPaging) {
        Sort sort = Sort.by(activityPaging.getSortDirection(), activityPaging.getSortBy());
        return PageRequest.of(activityPaging.getPageNumber(), activityPaging.getPageSize(), sort);
    }

    private long getActivityCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Activity> countRoot = countQuery.from(Activity.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

}
