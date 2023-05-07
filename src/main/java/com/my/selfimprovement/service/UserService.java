package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.util.LoadedFile;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    long count();

    /**
     * Saves {@code file} and assigns file name to user avatar field
     * @param file name of file to be set
     * @param userId user id
     */
    @PreAuthorize("isAuthenticated()")
    void setAvatar(MultipartFile file, long userId) throws IOException;

    Optional<LoadedFile> getAvatar(long userId) throws IOException;

    @PreAuthorize("isAuthenticated()")
    void removeAvatar(long userId) throws IOException;

}
