package com.my.selfimprovement.service;

import com.my.selfimprovement.entity.Category;
import com.my.selfimprovement.util.exception.CategoryNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROOT')")
    Category create(String categoryName);

    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROOT')")
    void remove(long categoryId) throws CategoryNotFoundException;

    List<Category> getAll();

    Optional<Category> getByName(String name);

    Category getByIdOrElseThrow(long id) throws CategoryNotFoundException;

}
