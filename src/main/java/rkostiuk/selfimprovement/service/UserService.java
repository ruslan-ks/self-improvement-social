package rkostiuk.selfimprovement.service;

import rkostiuk.selfimprovement.dto.request.UserUpdateRequest;
import rkostiuk.selfimprovement.entity.User;
import rkostiuk.selfimprovement.repository.filter.FilterCriteria;
import rkostiuk.selfimprovement.repository.filter.UserPageRequest;
import rkostiuk.selfimprovement.util.LoadedFile;
import rkostiuk.selfimprovement.util.exception.AvatarNotFoundException;
import rkostiuk.selfimprovement.util.exception.FileException;
import rkostiuk.selfimprovement.util.exception.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import rkostiuk.selfimprovement.util.exception.FilterCriteriaException;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface UserService {

    /**
     * Returns User with a specified email if found
     * @param email user email
     * @return found User optional
     */
    Optional<User> getByEmail(String email);

    /**
     * Parses {@code criteriaQuery} and calls {@link UserService#getPage(UserPageRequest, List)} to get page
     * @param pageRequest pagination parameters object
     * @param criteriaQuery query to be parsed
     * @return resulting page
     * @throws IllegalArgumentException if criteriaQuery is invalid
     * @throws FilterCriteriaException if filtering fails
     */
    Page<User> getPage(UserPageRequest pageRequest, String criteriaQuery);

    /**
     * Returns user page containing users that match every criteria in {@code criteriaList}
     * @param pageRequest pagination parameters object
     * @param filterCriteriaList criteria list
     * @return resulting page
     * @throws FilterCriteriaException if filtering fails
     */
    Page<User> getPage(UserPageRequest pageRequest, List<FilterCriteria> filterCriteriaList);

    void save(@Valid User user);

    User update(long userId, @Valid UserUpdateRequest userUpdateRequest);

    long count();

    User getByIdOrElseThrow(long userId) throws UserNotFoundException;

    /**
     * Saves {@code file} and assigns file name to user avatar field
     * @param file name of file to be set
     * @param userId user id
     * @throws FileException if file cannot be saved as defined
     * by {@link FileService#saveToUploads(MultipartFile, long, Predicate)}
     */
    @PreAuthorize("isAuthenticated()")
    void setAvatar(MultipartFile file, long userId) throws UserNotFoundException, FileException;

    /**
     * Returns user avatar LoadedFile if one is present
     * @param userId user id
     * @return Optional containing LoadedFile which represents user avatar
     * @throws FileException if file cannot be read as defined
     * by {@link FileService#getLoadedFile(String)}
     */
    Optional<LoadedFile> getAvatar(long userId) throws UserNotFoundException, FileException;

    /**
     * Removes avatar if one is present
     * @param userId user id
     * @throws AvatarNotFoundException if user.avatar is null
     * @throws FileException if file cannot be removed as defined
     * by {@link FileService#removeFromUploads(String)}
     */
    @PreAuthorize("isAuthenticated()")
    void removeAvatar(long userId) throws UserNotFoundException, AvatarNotFoundException, FileException;

    long getFollowersCount(long userId) throws UserNotFoundException;

    Stream<User> getFollowersPage(long userId, Pageable pageable) throws UserNotFoundException;

    long getFollowingsCount(long userId) throws UserNotFoundException;

    Stream<User> getFollowingsPage(long userId, Pageable pageable) throws UserNotFoundException;

    List<Long> getFollowingIds(long userId) throws UserNotFoundException;

    /**
     * Adds following to user followings
     * @param userId user
     * @param newFollowerId follower to be added
     * @throws IllegalArgumentException if userId == newFollowerId
     */
    void addFollower(long userId, long newFollowerId) throws UserNotFoundException;

    /**
     * Removes user with id {@code userId} from followers of user with id {@code followerId}
     * @param userId user id
     * @param followerId follower to be removed id
     * @throws IllegalArgumentException if userId == followerId
     * @throws java.util.NoSuchElementException if user has no such following
     */
    void removeFollower(long userId, long followerId);

}
