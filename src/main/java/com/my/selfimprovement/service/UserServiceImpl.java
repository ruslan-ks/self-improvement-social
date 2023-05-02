package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.repository.UserRepository;
import com.my.selfimprovement.util.validation.UserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@Validated
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserValidator userValidator;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Stream<User> findActiveUsersPage(Pageable pageable) {
        return userRepository.findByStatus(User.Status.ACTIVE, pageable).stream();
    }

    @Transactional
    @Override
    public void save(@Valid User user) {
        userValidator.validate(user);
        userRepository.save(user);
    }

    @Override
    public long count() {
        return userRepository.count();
    }
}
