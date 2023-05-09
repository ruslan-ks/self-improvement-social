package com.my.selfimprovement.service;

import com.my.selfimprovement.dto.request.UserUpdateRequest;
import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.repository.UserRepository;
import com.my.selfimprovement.util.LoadedFile;
import com.my.selfimprovement.util.exception.UserNotFoundException;
import com.my.selfimprovement.util.validation.UserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@Validated
@RequiredArgsConstructor
@Slf4j
public class SpringDataUserService implements UserService {

    private final UserRepository userRepository;

    private final UserValidator userValidator;

    private final FileService fileService;

    private static final Set<MediaType> allowedAvatarMediaTypes = Set.of(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG);

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Stream<User> findActiveUsersPage(Pageable pageable) {
        return userRepository.findByStatus(User.Status.ACTIVE, pageable).stream();
    }

    @Override
    @Transactional
    public void save(@Valid User user) {
        userValidator.validate(user);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(long userId, UserUpdateRequest userUpdateRequest) {
        User user = findByIdOrElseThrow(userId);
        user.setName(userUpdateRequest.name());
        user.setSurname(userUpdateRequest.surname());
        user.setBirthday(userUpdateRequest.birthday());
        return user;
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public Optional<User> findById(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    @Transactional
    public void setAvatar(MultipartFile file, long userId) {
        User user = findByIdOrElseThrow(userId);
        String fileName = fileService.saveToUploads(file, userId, allowedAvatarMediaTypes::contains);
        try {
            String oldAvatarFileName = user.getAvatarFileName();
            user.setAvatarFileName(fileName);
            if (oldAvatarFileName != null) {
                fileService.removeFromUploads(oldAvatarFileName);
            }
        } catch (Exception ex) {
            log.error("Failed to set user avatar. Exception occurred: ", ex);
            fileService.removeFromUploads(fileName);
            throw ex;
        }
    }

    @Override
    public Optional<LoadedFile> getAvatar(long userId) {
        User user = findByIdOrElseThrow(userId);
        return Optional.ofNullable(user.getAvatarFileName())
                .map(fileService::getLoadedFile);
    }

    @Override
    @Transactional
    public void removeAvatar(long userId) {
        User user = findByIdOrElseThrow(userId);
        String avatarFileName = user.getAvatarFileName();
        if (avatarFileName == null) {
            throw new NoSuchElementException("User with id " + userId + " has no avatar");
        }
        fileService.removeFromUploads(avatarFileName);
        user.setAvatarFileName(null);
    }

    @Override
    public long getFollowersCount(long userId) {
        User user = findByIdOrElseThrow(userId);
        return user.getFollowers().size();
    }

    @Override
    public Stream<User> getFollowersPage(long userId, Pageable pageable) {
        User user = findByIdOrElseThrow(userId);
        return userRepository.findByFollowingsContaining(user, pageable).stream();
    }

    @Override
    public long getFollowingsCount(long userId) {
        User user = findByIdOrElseThrow(userId);
        return user.getFollowings().size();
    }

    @Override
    public Stream<User> getFollowingsPage(long userId, Pageable pageable) {
        User user = findByIdOrElseThrow(userId);
        return userRepository.findByFollowersContaining(user, pageable).stream();
    }

    @Override
    @Transactional
    public void addFollowing(long userId, long newFollowingUserId) {
        if (userId == newFollowingUserId) {
            throw new IllegalArgumentException("Cannot add following: illegal args: userId == newFollowingUserId: " +
                    userId);
        }
        User user = findByIdOrElseThrow(userId);
        User userToBeFollowed = findByIdOrElseThrow(newFollowingUserId);
        user.addFollowing(userToBeFollowed);
    }

    @Override
    @Transactional
    public void removeFollower(long userId, long followerId) {
        if (userId == followerId) {
            throw new IllegalArgumentException("Cannot remove follower: illegal args: " + "userId == followerId: " +
                    userId);
        }
        User user = findByIdOrElseThrow(userId);
        User followerToRemove = findByIdOrElseThrow(followerId);
        boolean removed = user.removeFollower(followerToRemove);
        if (!removed) {
            throw new NoSuchElementException("Cannot remove follower. User with id " + followerId +
                    " is not subscribed to user with id " + userId);
        }
    }

    private User findByIdOrElseThrow(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found. User id: " + userId));
    }

}
