package com.my.selfimprovement.service;

import com.my.selfimprovement.dto.request.NewActivityRequest;
import com.my.selfimprovement.entity.Activity;
import com.my.selfimprovement.repository.filter.ActivityPageRequest;
import com.my.selfimprovement.repository.filter.FilterCriteria;
import com.my.selfimprovement.util.exception.ActivityNotFoundException;
import com.my.selfimprovement.util.exception.CategoryNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ActivityService {

    /**
     * Creates new activity
     * @param activityRequest activity data
     * @param authorId activity author id
     * @return newly created activity
     * @throws com.my.selfimprovement.util.exception.CategoryNotFoundException if activityRequest contains
     * category id that does not exist
     */
    @PreAuthorize("isAuthenticated()")
    Activity create(NewActivityRequest activityRequest, long authorId) throws CategoryNotFoundException;

    Stream<Activity> getPage(Pageable pageable);

    Page<Activity> getPage(ActivityPageRequest pageRequest, List<FilterCriteria> filterCriteriaList);

    Optional<Activity> getById(long id);

    Activity getByIdOrElseThrow(long id) throws ActivityNotFoundException;

    long count();

}
