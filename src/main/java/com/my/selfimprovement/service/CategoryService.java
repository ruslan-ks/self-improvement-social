package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.Category;
import jakarta.validation.Valid;

import java.util.List;

public interface CategoryService {

    void save(@Valid Category category);

    /**
     * Removes category
     * @param categoryId category to remove id
     * @throws com.my.selfimprovement.util.exception.CategoryNotFoundException if category with specified id not found
     */
    void remove(long categoryId);

    List<Category> getAll();

}
