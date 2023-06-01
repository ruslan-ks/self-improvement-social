package rkostiuk.selfimprovement.service;

import rkostiuk.selfimprovement.dto.request.NewActivityRequest;
import rkostiuk.selfimprovement.entity.Activity;
import rkostiuk.selfimprovement.repository.filter.ActivityPageRequest;
import rkostiuk.selfimprovement.repository.filter.FilterCriteria;
import rkostiuk.selfimprovement.util.exception.ActivityNotFoundException;
import rkostiuk.selfimprovement.util.exception.CategoryNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import rkostiuk.selfimprovement.util.exception.FilterCriteriaException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ActivityService {

    /**
     * Creates new activity
     * @param activityRequest activity data
     * @param authorId activity author id
     * @return newly created activity
     * @throws CategoryNotFoundException if activityRequest contains
     * category id that does not exist
     */
    @PreAuthorize("isAuthenticated()")
    Activity create(NewActivityRequest activityRequest, long authorId) throws CategoryNotFoundException;

    Stream<Activity> getPage(Pageable pageable);

    /**
     * Returns activity page containing activities that match every criteria in {@code criteriaList}
     * @param pageRequest pagination parameters object
     * @param filterCriteriaList criteria list
     * @return resulting page
     * @throws FilterCriteriaException if filtering fails
     */
    Page<Activity> getPage(ActivityPageRequest pageRequest, List<FilterCriteria> filterCriteriaList);

    /**
     * Parses {@code criteriaString} and calls {@link ActivityService#getPage(ActivityPageRequest, List)} to get page
     * @param pageRequest pagination parameters object
     * @param criteriaQuery query to be parsed
     * @return resulting page
     * @throws IllegalArgumentException if criteriaQuery is invalid
     * @throws FilterCriteriaException if filtering fails
     */
    Page<Activity> getPage(ActivityPageRequest pageRequest, String criteriaQuery);

    Optional<Activity> getById(long id);

    Activity getByIdOrElseThrow(long id) throws ActivityNotFoundException;

    long count();

}
