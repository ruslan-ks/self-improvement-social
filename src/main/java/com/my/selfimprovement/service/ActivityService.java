package com.my.selfimprovement.service;

import com.my.selfimprovement.dto.request.NewActivityRequest;
import com.my.selfimprovement.entity.Activity;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;
import java.util.stream.Stream;

public interface ActivityService {

    /**
     * Creates new activity
     * @param activityRequest activity data
     * @param authorId activity author id
     * @return newly created activity
     * @throws com.my.selfimprovement.util.exception.UserNotFoundException if user with specified id does not exist
     * @throws com.my.selfimprovement.util.exception.CategoryNotFoundException if activityRequest contains
     * category id that does not exist
     */
    @PreAuthorize("isAuthenticated()")
    Activity create(NewActivityRequest activityRequest, long authorId);

    Stream<Activity> getPage(Pageable pageable);

    Optional<Activity> getById(long id);

    long count();

}
