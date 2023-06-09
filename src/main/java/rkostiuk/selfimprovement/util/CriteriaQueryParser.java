package rkostiuk.selfimprovement.util;

import rkostiuk.selfimprovement.repository.filter.FilterCriteria;
import rkostiuk.selfimprovement.repository.filter.FilterOperator;

import java.util.List;

/**
 * Parses criteria query.
 * Valid query examples:
 * <ul>
 *     <li>minutesRequired eq 60</li>
 *     <li>name like hello; minutesRequired ge 45</li>
 *     <li>name like apple;categories cn 1;categories cn 2</li>
 *     <li>description like semicolon: \; - and it's ok!</li>
 * </ul>
 * @see FilterOperator
 * @see FilterCriteria
 */
public interface CriteriaQueryParser {
    /**
     * Parses criteria query string
     * @param criteriaQuery string to parse
     * @return list of FilterCriteria, or Collections.emptyList() if {@code criteriaQuery} is null or blank
     */
    List<FilterCriteria> parse(String criteriaQuery);
}
