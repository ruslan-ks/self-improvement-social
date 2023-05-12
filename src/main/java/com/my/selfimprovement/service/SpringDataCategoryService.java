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
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Validated
@RequiredArgsConstructor
@Slf4j
public class SpringDataCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category create(String categoryName) {
        Category category = newCategory(categoryName);
        categoryRepository.save(category);
        return category;
    }

    private Category newCategory(String name) {
        var category = new Category();
        category.setName(name);
        return category;
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public void remove(long categoryId) {
        Category category = getByIdOrElseThrow(categoryId);
        categoryRepository.delete(category);
    }

    @Override
    public Optional<Category> getByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category getByIdOrElseThrow(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Cannot find category with id: " + categoryId));
    }

}
