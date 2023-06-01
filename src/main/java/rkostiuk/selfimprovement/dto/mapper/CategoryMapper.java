package rkostiuk.selfimprovement.dto.mapper;

import rkostiuk.selfimprovement.dto.response.ShortCategoryResponse;
import rkostiuk.selfimprovement.entity.Category;

public interface CategoryMapper {

    ShortCategoryResponse toShortCategoryResponse(Category category);

}
