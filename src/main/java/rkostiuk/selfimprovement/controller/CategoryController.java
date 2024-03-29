package rkostiuk.selfimprovement.controller;

import rkostiuk.selfimprovement.dto.mapper.CategoryMapper;
import rkostiuk.selfimprovement.dto.request.NewCategoryRequest;
import rkostiuk.selfimprovement.dto.response.ResponseBody;
import rkostiuk.selfimprovement.dto.response.ShortCategoryResponse;
import rkostiuk.selfimprovement.entity.Category;
import rkostiuk.selfimprovement.service.CategoryService;
import rkostiuk.selfimprovement.util.validation.NewCategoryValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;

    private final NewCategoryValidator newCategoryValidator;

    @GetMapping
    public ResponseBody getAll() {
        List<ShortCategoryResponse> shortCategoryResponses = categoryService.getAll()
                .stream()
                .map(categoryMapper::toShortCategoryResponse)
                .toList();
        return ResponseBody.ok("categories", shortCategoryResponses);
    }

    @PostMapping
    public ResponseBody create(@RequestBody @Valid NewCategoryRequest request) {
        newCategoryValidator.validate(request);
        Category category = categoryService.create(request.name());
        ShortCategoryResponse categoryResponse = categoryMapper.toShortCategoryResponse(category);
        return ResponseBody.ok("category", categoryResponse);
    }

    @DeleteMapping("{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long categoryId) {
        categoryService.remove(categoryId);
    }

}
