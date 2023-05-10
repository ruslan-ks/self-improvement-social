package com.my.selfimprovement.controller;

import com.my.selfimprovement.dto.mapper.CategoryMapper;
import com.my.selfimprovement.dto.response.ResponseBody;
import com.my.selfimprovement.dto.response.ShortCategoryResponse;
import com.my.selfimprovement.service.CategoryService;
import com.my.selfimprovement.util.HttpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoriesController {

    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<ResponseBody> getAll() {
        List<ShortCategoryResponse> shortCategoryResponses = categoryService.getAll()
                .stream()
                .map(categoryMapper::toShortCategoryResponse)
                .toList();
        return HttpUtils.ok(Map.of("categories", shortCategoryResponses));
    }

}
