package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.util.LoadedFile;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.function.Predicate;
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
     * @throws com.my.selfimprovement.util.exception.UserNotFoundException if user is not found in the db
     * @throws com.my.selfimprovement.util.exception.FileException if file cannot be saved as defined
     * by {@link FileService#saveToUploads(MultipartFile, long, Predicate)}
     */
    @PreAuthorize("isAuthenticated()")
    void setAvatar(MultipartFile file, long userId);

    /**
     * Returns user avatar LoadedFile if one is present
     * @param userId user id
     * @return Optional containing LoadedFile which represents user avatar
     * @throws com.my.selfimprovement.util.exception.UserNotFoundException if user is not found in the db
     * @throws com.my.selfimprovement.util.exception.FileException if file cannot be read as defined
     * by {@link FileService#getLoadedFile(String)}
     */
    Optional<LoadedFile> getAvatar(long userId);

    /**
     * Removes avatar if one is present
     * @param userId user id
     * @throws com.my.selfimprovement.util.exception.UserNotFoundException if user is not found in the db
     * @throws java.util.NoSuchElementException if user.avatar is null
     * @throws com.my.selfimprovement.util.exception.FileException if file cannot be removed as defined
     * by {@link FileService#removeFromUploads(String)}
     */
    @PreAuthorize("isAuthenticated()")
    void removeAvatar(long userId);

}
