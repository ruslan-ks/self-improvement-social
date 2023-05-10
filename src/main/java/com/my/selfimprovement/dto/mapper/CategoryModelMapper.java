package com.my.selfimprovement.dto.mapper;

import com.my.selfimprovement.dto.response.ShortCategoryResponse;
import com.my.selfimprovement.entity.Category;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryModelMapper implements CategoryMapper {

    private final ModelMapper modelMapper;

    @Override
    public ShortCategoryResponse toShortCategoryResponse(Category category) {
        return modelMapper.map(category, ShortCategoryResponse.class);
    }

}
