package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.Category;
import com.my.selfimprovement.repository.CategoryRepository;
import com.my.selfimprovement.util.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Validated
@RequiredArgsConstructor
@Slf4j
public class SpringDataCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void remove(long categoryId) {
        Category category = findByIdOrElseThrow(categoryId);
        categoryRepository.delete(category);
    }

    private Category findByIdOrElseThrow(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Cannot find category with id: " + categoryId));
    }

}
