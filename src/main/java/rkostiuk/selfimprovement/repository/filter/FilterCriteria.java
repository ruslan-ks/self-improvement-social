package rkostiuk.selfimprovement.repository.filter;

public record FilterCriteria(String field, FilterOperation operation, Object value) {}
