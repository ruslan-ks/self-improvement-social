package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.UserActivity;
import com.my.selfimprovement.util.exception.ActivityNotFoundException;
import com.my.selfimprovement.util.exception.UserActivityNotFoundException;
import com.my.selfimprovement.util.exception.UserNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.stream.Stream;

public interface UserActivityService {

    Stream<UserActivity> getPage(long userId, Pageable pageable) throws UserNotFoundException;

    long count(long userId) throws UserNotFoundException;

    /**
     * Add activity to user activities
     *
     * @throws IllegalStateException if activity is already added
     */
    @PreAuthorize("isAuthenticated()")
    void add(long activityId, long userId)
            throws ActivityNotFoundException, UserNotFoundException, IllegalStateException;

    UserActivity get(long userId, long activityId) throws UserActivityNotFoundException;

}
