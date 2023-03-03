package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * Returns User with a specified email if found
     * @param email user email
     * @return found User optional
     */
    Optional<User> findByEmail(String email);

    void save(User user);

    List<User> findAll();

}
