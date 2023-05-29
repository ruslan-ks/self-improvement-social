package com.my.selfimprovement.util;

import com.my.selfimprovement.repository.filter.FilterCriteria;
import com.my.selfimprovement.repository.filter.FilterOperation;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegexCriteriaQueryParser implements CriteriaQueryParser {

    private static final Pattern SEPARATOR_PATTERN = Pattern.compile("(?<!\\\\);+");
    private static final Pattern CRITERIA_EXPRESSION_PATTERN = Pattern.compile("(\\w+) +(\\w+) +(.+)");

    @Override
    public List<FilterCriteria> parse(String criteriaQuery) {
        if (criteriaQuery == null || criteriaQuery.isBlank()) {
            return Collections.emptyList();
        }
        return SEPARATOR_PATTERN.splitAsStream(criteriaQuery)
                .map(CRITERIA_EXPRESSION_PATTERN::matcher)
                .map(RegexCriteriaQueryParser::buildCriteria)
                .toList();
    }

    private static FilterCriteria buildCriteria(Matcher matcher) {
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid query!");
        }
        return new FilterCriteria(matcher.group(1), FilterOperation.fromCode(matcher.group(2)), matcher.group(3));
    }

}
