package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.stream.Stream;

public interface UserService {

    /**
     * Returns User with a specified email if found
     * @param email user email
     * @return found User optional
     */
    Optional<User> findByEmail(String email);

    Stream<User> findActiveUsersPage(Pageable pageable);

    void save(@Valid User user);

}
