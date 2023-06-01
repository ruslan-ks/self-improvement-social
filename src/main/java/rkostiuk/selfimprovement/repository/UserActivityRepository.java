package rkostiuk.selfimprovement.repository;

import rkostiuk.selfimprovement.entity.Activity;
import rkostiuk.selfimprovement.entity.User;
import rkostiuk.selfimprovement.entity.UserActivity;
import rkostiuk.selfimprovement.entity.key.UserActivityPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, UserActivityPK> {

    Page<UserActivity> findUserActivitiesByUser(User user, Pageable pageable);

    long countUserActivitiesByUser(User user);

    boolean existsByActivityAndUser(Activity activity, User user);

}
