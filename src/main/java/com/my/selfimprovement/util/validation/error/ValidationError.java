package com.my.selfimprovement.util.validation.error;

public record ValidationError(String field, RejectedValue<?> rejectedValue) {}
