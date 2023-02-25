package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.User;

import java.util.List;

public interface UserService {

    /**
     * Returns User with a specified email, throws {@link com.my.selfimprovement.util.exception.UserNotFoundException}
     * if not found
     * @param email user email
     * @return found User
     * @throws com.my.selfimprovement.util.exception.UserNotFoundException if there is no such User
     */
    User findByEmail(String email);

    void save(User user);

    List<User> findAll();

}
