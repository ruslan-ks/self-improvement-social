package com.my.selfimprovement.repository.filter;

import java.util.Map;

public enum FilterOperation {
    EQUAL, NOT_EQUAL, LIKE, CONTAINS, DOES_NOT_CONTAIN, NULL, NOT_NULL, GREATER_EQUAL, LESS_EQUAL;

    private static final Map<FilterOperation, String> OPERATION_CODE_MAP = Map.of(
            EQUAL, "eq",
            NOT_EQUAL, "ne",
            LIKE, "lk",
            CONTAINS, "cn",
            DOES_NOT_CONTAIN, "nc",
            NULL, "n",
            NOT_NULL, "nn",
            GREATER_EQUAL, "ge",
            LESS_EQUAL, "le"
    );

    public static String getOperationCode(FilterOperation operation) {
        return OPERATION_CODE_MAP.get(operation);
    }

    public static FilterOperation fromCode(String operationCode) {
        return OPERATION_CODE_MAP.entrySet().stream()
                .filter(e -> e.getValue().equals(operationCode))
                .findAny()
                .map(Map.Entry::getKey)
                .orElseThrow();
    }

}
