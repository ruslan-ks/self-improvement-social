package rkostiuk.selfimprovement.dto.request;

import jakarta.validation.constraints.Size;

public record NewCategoryRequest(@Size(min = 2, max = 128, message = "{valid.category.name.size}") String name) {}
