package rkostiuk.selfimprovement.repository.filter;

import rkostiuk.selfimprovement.util.exception.FilterCriteriaException;

import java.util.Map;

public enum FilterOperator {
    EQUAL, NOT_EQUAL, LIKE, CONTAINS, NULL, NOT_NULL, GREATER_EQUAL, LESS_EQUAL;

    private static final Map<FilterOperator, String> OPERATION_CODE_MAP = Map.of(
            EQUAL, "eq",
            NOT_EQUAL, "ne",
            LIKE, "lk",
            CONTAINS, "cn",
            NULL, "n",
            NOT_NULL, "nn",
            GREATER_EQUAL, "ge",
            LESS_EQUAL, "le"
    );

    public static String getOperationCode(FilterOperator operation) {
        return OPERATION_CODE_MAP.get(operation);
    }

    public static FilterOperator fromCode(String operationCode) {
        return OPERATION_CODE_MAP.entrySet().stream()
                .filter(e -> e.getValue().equals(operationCode))
                .findAny()
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new FilterCriteriaException("Unknown operation '" + operationCode + "'"));
    }

}
