package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.Category;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROOT')")
    Category create(String categoryName);

    /**
     * Removes category
     * @param categoryId category to remove id
     * @throws com.my.selfimprovement.util.exception.CategoryNotFoundException if category with specified id not found
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROOT')")
    void remove(long categoryId);

    List<Category> getAll();

    Optional<Category> getByName(String name);

}
