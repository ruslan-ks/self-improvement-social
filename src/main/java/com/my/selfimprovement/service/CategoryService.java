package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Category create(String categoryName);

    /**
     * Removes category
     * @param categoryId category to remove id
     * @throws com.my.selfimprovement.util.exception.CategoryNotFoundException if category with specified id not found
     */
    void remove(long categoryId);

    List<Category> getAll();

    Optional<Category> getByName(String name);

    /**
     * @throws com.my.selfimprovement.util.exception.CategoryNotFoundException if category with specified id not found
     */
    Category getByIdOrElseThrow(long id);

}
