package com.my.selfimprovement.service;

import com.my.selfimprovement.dto.mapper.ActivityMapper;
import com.my.selfimprovement.dto.request.NewActivityRequest;
import com.my.selfimprovement.entity.Activity;
import com.my.selfimprovement.entity.Category;
import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.repository.ActivityRepository;
import com.my.selfimprovement.repository.dao.CriteriaDao;
import com.my.selfimprovement.repository.filter.ActivityPageRequest;
import com.my.selfimprovement.repository.filter.FilterCriteria;
import com.my.selfimprovement.util.CriteriaQueryParser;
import com.my.selfimprovement.util.exception.ActivityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SpringDataActivityService implements ActivityService {

    private final ActivityRepository activityRepository;

    private final CriteriaDao<Activity> activityCriteriaDao;

    private final UserService userService;

    private final CategoryService categoryService;

    private final ActivityMapper activityMapper;

    private final CriteriaQueryParser criteriaQueryParser;

    @Override
    @Transactional
    public Activity create(NewActivityRequest activityRequest, long authorId) {
        User author = userService.getByIdOrElseThrow(authorId);
        Set<Category> categories = activityRequest.getCategoryIds().stream()
                .map(categoryService::getByIdOrElseThrow)
                .collect(Collectors.toSet());
        Activity activity = activityMapper.toActivity(activityRequest);
        activity.setAuthor(author);
        activity.addCategories(categories);
        categories.forEach(c -> c.addActivity(activity));
        activityRepository.save(activity);
        log.info("Activity created: {}", activity);
        return activity;
    }

    @Override
    public Stream<Activity> getPage(Pageable pageable) {
        return activityRepository.findAll(pageable).stream();
    }

    @Override
    public Page<Activity> getPage(ActivityPageRequest pageRequest, List<FilterCriteria> filterCriteriaList) {
        return activityCriteriaDao.getPage(pageRequest, filterCriteriaList);
    }

    @Override
    public Page<Activity> getPage(ActivityPageRequest pageRequest, String criteriaQuery) {
        List<FilterCriteria> filterCriteriaList = criteriaQueryParser.parse(criteriaQuery);
        return getPage(pageRequest, filterCriteriaList);
    }

    @Override
    public long count() {
        return activityRepository.count();
    }

    @Override
    public Optional<Activity> getById(long id) {
        return activityRepository.findById(id);
    }

    @Override
    public Activity getByIdOrElseThrow(long id) throws ActivityNotFoundException {
        return activityRepository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException("Activity with id " + id + " not found"));
    }

}
