package com.my.selfimprovement.repository;

import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.entity.UserActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    Page<UserActivity> findUserActivitiesByUser(User user, Pageable pageable);

}
