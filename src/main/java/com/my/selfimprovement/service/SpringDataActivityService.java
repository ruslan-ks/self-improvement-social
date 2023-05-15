package com.my.selfimprovement.service;

import com.my.selfimprovement.dto.mapper.ActivityMapper;
import com.my.selfimprovement.dto.request.NewActivityRequest;
import com.my.selfimprovement.entity.Activity;
import com.my.selfimprovement.entity.Category;
import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpringDataActivityService implements ActivityService {

    private final ActivityRepository activityRepository;

    private final UserService userService;

    private final CategoryService categoryService;

    private final ActivityMapper activityMapper;

    @Override
    public Activity create(NewActivityRequest activityRequest, long authorId) {
        User author = userService.findByIdOrElseThrow(authorId);
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

}
