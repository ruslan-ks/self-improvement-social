package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.repository.UserRepository;
import com.my.selfimprovement.util.LoadedFile;
import com.my.selfimprovement.util.validation.UserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@Validated
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

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
    public long count() {
        return userRepository.count();
    }

    @Override
    @Transactional
    public void setAvatar(MultipartFile file, long userId) throws IOException {
        String fileName = fileService.saveToUploads(file, userId, allowedAvatarMediaTypes::contains);
        User user = findByIdOrElseThrow(userId);
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
                .map(this::getLoadedFileSneaky);
    }

    @SneakyThrows
    private LoadedFile getLoadedFileSneaky(String fileName) {
        return fileService.getLoadedFile(fileName);
    }

    @Override
    @Transactional
    public void removeAvatar(long userId) throws IOException {
        User user = findByIdOrElseThrow(userId);
        String avatarFileName = user.getAvatarFileName();
        if (avatarFileName != null) {
            fileService.removeFromUploads(avatarFileName);
            user.setAvatarFileName(null);
        }
    }

    private User findByIdOrElseThrow(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found. User id: " + userId));
    }

}
