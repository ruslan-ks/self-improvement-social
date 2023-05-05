package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.repository.UserRepository;
import com.my.selfimprovement.util.validation.UserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;
import java.util.Optional;
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
    public void setUserAvatar(MultipartFile file, long userId) {
        String fileName = fileService.saveToUploads(file, userId);
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User not found. User id: " + userId));
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

}
