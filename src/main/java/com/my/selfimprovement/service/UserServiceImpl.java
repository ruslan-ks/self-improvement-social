package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.repository.UserRepository;
import com.my.selfimprovement.util.validation.ThrowingUserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Validated
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ThrowingUserValidator userValidator;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public void save(@Valid User user) {
        userValidator.validate(user);
        userRepository.save(user);
    }

}
