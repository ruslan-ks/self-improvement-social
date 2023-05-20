package com.my.selfimprovement.util.validation.error;

import java.util.List;

public record ValidationError(String field, List<RejectedValueMessage<?>> rejectedValues) {}
