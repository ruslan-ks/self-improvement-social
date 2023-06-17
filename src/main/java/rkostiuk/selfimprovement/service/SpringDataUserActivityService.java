package rkostiuk.selfimprovement.service;

import rkostiuk.selfimprovement.entity.Activity;
import rkostiuk.selfimprovement.entity.User;
import rkostiuk.selfimprovement.entity.UserActivity;
import rkostiuk.selfimprovement.entity.UserActivityCompletion;
import rkostiuk.selfimprovement.entity.key.UserActivityPK;
import rkostiuk.selfimprovement.repository.UserActivityRepository;
import rkostiuk.selfimprovement.util.exception.ActivityNotFoundException;
import rkostiuk.selfimprovement.util.exception.UserActivityNotFoundException;
import rkostiuk.selfimprovement.util.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        User user = userService.getByIdOrElseThrow(userId);
        return userActivityRepository.findUserActivitiesByUser(user, pageable).stream();
    }

    @Override
    public List<Long> getActivityIds(long userId) throws UserNotFoundException {
        return userService.getByIdOrElseThrow(userId).getActivities().stream()
                .map(UserActivity::getUserActivityPK)
                .map(UserActivityPK::getActivityId)
                .toList();
    }

    @Override
    public long count(long userId) {
        User user = userService.getByIdOrElseThrow(userId);
        return userActivityRepository.countUserActivitiesByUser(user);
    }

    @Override
    @Transactional
    public void add(long activityId, long userId)
            throws ActivityNotFoundException, UserNotFoundException, IllegalStateException {
        Activity activity = activityService.getByIdOrElseThrow(activityId);
        User user = userService.getByIdOrElseThrow(userId);
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
        UserActivity userActivity = getByKeyOrElseThrow(userId, activityId);
        userActivityRepository.delete(userActivity);
    }

    @Override
    public UserActivity getByKeyOrElseThrow(long userId, long activityId) throws UserActivityNotFoundException {
        return userActivityRepository.findById(new UserActivityPK(userId, activityId))
                .orElseThrow(() -> new UserActivityNotFoundException("User activity not found. userId: " + userId +
                        ", activityId: " + activityId));
    }

    @Override
    @Transactional
    public void addCompletion(long userId, long activityId) throws UserActivityNotFoundException {
        UserActivity userActivity = getByKeyOrElseThrow(userId, activityId);
        Activity activity = userActivity.getActivity();
        if (!activity.mayBeCompleted(userActivity)) {
            throw new IllegalStateException("Activity completion not allowed");
        }
        var completion = new UserActivityCompletion();
        completion.setUserActivity(userActivity);
        userActivity.addCompletion(completion);
    }

}
