package rkostiuk.selfimprovement.repository.filter;

public record FilterCriteria(String field, FilterOperator operation, Object value) {}
