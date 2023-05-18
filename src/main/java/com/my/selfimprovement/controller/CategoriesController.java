package com.my.selfimprovement.controller;

import com.my.selfimprovement.dto.mapper.CategoryMapper;
import com.my.selfimprovement.dto.request.NewCategoryRequest;
import com.my.selfimprovement.dto.response.ResponseBody;
import com.my.selfimprovement.dto.response.ShortCategoryResponse;
import com.my.selfimprovement.entity.Category;
import com.my.selfimprovement.service.CategoryService;
import com.my.selfimprovement.util.HttpUtils;
import com.my.selfimprovement.util.validation.NewCategoryValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoriesController {

    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;

    private final NewCategoryValidator newCategoryValidator;

    @GetMapping
    public ResponseEntity<ResponseBody> getAll() {
        List<ShortCategoryResponse> shortCategoryResponses = categoryService.getAll()
                .stream()
                .map(categoryMapper::toShortCategoryResponse)
                .toList();
        return HttpUtils.ok(Map.of("categories", shortCategoryResponses));
    }

    @PostMapping
    public ResponseEntity<ResponseBody> create(@RequestBody @Valid NewCategoryRequest request) {
        newCategoryValidator.validate(request);
        Category category = categoryService.create(request.name());
        ShortCategoryResponse categoryResponse = categoryMapper.toShortCategoryResponse(category);
        return HttpUtils.ok(Map.of("category", categoryResponse));
    }

    @DeleteMapping("{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long categoryId) {
        categoryService.remove(categoryId);
    }

}
