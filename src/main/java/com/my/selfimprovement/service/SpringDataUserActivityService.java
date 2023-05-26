package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.Activity;
import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.entity.UserActivity;
import com.my.selfimprovement.entity.key.UserActivityPK;
import com.my.selfimprovement.repository.UserActivityRepository;
import com.my.selfimprovement.util.exception.ActivityNotFoundException;
import com.my.selfimprovement.util.exception.UserActivityNotFoundException;
import com.my.selfimprovement.util.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SpringDataUserActivityService implements UserActivityService {

    private final UserActivityRepository userActivityRepository;

    private final UserService userService;

    private final ActivityService activityService;

    @Override
    public Stream<UserActivity> getPage(long userId, Pageable pageable) {
        User user = userService.findByIdOrElseThrow(userId);
        return userActivityRepository.findUserActivitiesByUser(user, pageable).stream();
    }

    @Override
    public long count(long userId) {
        User user = userService.findByIdOrElseThrow(userId);
        return userActivityRepository.countUserActivitiesByUser(user);
    }

    @Override
    @Transactional
    public void add(long activityId, long userId)
            throws ActivityNotFoundException, UserNotFoundException, IllegalStateException {
        Activity activity = activityService.getByIdOrElseThrow(activityId);
        User user = userService.findByIdOrElseThrow(userId);
        if (userActivityRepository.existsByActivityAndUser(activity, user)) {
            throw new IllegalStateException("User activity already exists(activity id: " + activityId +
                    ", user id: " + userId);
        }
        var userActivity = new UserActivity(user, activity);
        userActivityRepository.save(userActivity);
    }

    @Override
    @Transactional
    public void delete(long userId, long activityId) throws UserActivityNotFoundException {
        UserActivity userActivity = get(userId, activityId);
        userActivityRepository.delete(userActivity);
    }

    @Override
    public UserActivity get(long userId, long activityId)
            throws UserNotFoundException, ActivityNotFoundException {
        return userActivityRepository.findById(new UserActivityPK(userId, activityId))
                .orElseThrow(() -> new UserActivityNotFoundException("User activity not found. userId: " + userId +
                        ", activityId: " + activityId));
    }

}
