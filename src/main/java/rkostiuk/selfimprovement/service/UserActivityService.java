package rkostiuk.selfimprovement.service;

import rkostiuk.selfimprovement.entity.Activity;
import rkostiuk.selfimprovement.entity.LimitedCompletionsActivity;
import rkostiuk.selfimprovement.entity.PeriodicalLimitedCompletionsActivity;
import rkostiuk.selfimprovement.entity.UserActivity;
import rkostiuk.selfimprovement.util.exception.ActivityNotFoundException;
import rkostiuk.selfimprovement.util.exception.UserActivityNotFoundException;
import rkostiuk.selfimprovement.util.exception.UserNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Stream;

public interface UserActivityService {

    Stream<UserActivity> getPage(long userId, Pageable pageable) throws UserNotFoundException;

    List<Long> getActivityIds(long userId) throws UserNotFoundException;

    long count(long userId) throws UserNotFoundException;

    /**
     * @throws IllegalStateException if activity is already added
     */
    @PreAuthorize("isAuthenticated()")
    void add(long activityId, long userId)
            throws ActivityNotFoundException, UserNotFoundException, IllegalStateException;

    @PreAuthorize("isAuthenticated()")
    void delete(long userId, long activityId) throws UserActivityNotFoundException;

    UserActivity getByKeyOrElseThrow(long userId, long activityId) throws UserActivityNotFoundException;

    /**
     * @throws IllegalStateException if activty completion not allowed.
     * @see Activity#mayBeCompleted(UserActivity)
     * @see LimitedCompletionsActivity#mayBeCompleted(UserActivity)
     * @see PeriodicalLimitedCompletionsActivity#mayBeCompleted(UserActivity)
     */
    @PreAuthorize("isAuthenticated()")
    void addCompletion(long userId, long activityId) throws UserActivityNotFoundException, IllegalStateException;

}
