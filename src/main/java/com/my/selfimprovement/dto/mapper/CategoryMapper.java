package com.my.selfimprovement.dto.mapper;

import com.my.selfimprovement.dto.response.ShortCategoryResponse;
import com.my.selfimprovement.entity.Category;

public interface CategoryMapper {

    ShortCategoryResponse toShortCategoryResponse(Category category);

}
