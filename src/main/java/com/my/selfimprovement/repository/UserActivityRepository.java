package com.my.selfimprovement.repository;

import com.my.selfimprovement.entity.Activity;
import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.entity.UserActivity;
import com.my.selfimprovement.entity.key.UserActivityPK;
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
