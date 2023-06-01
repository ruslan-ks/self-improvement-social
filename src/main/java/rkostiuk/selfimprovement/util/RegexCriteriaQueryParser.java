package rkostiuk.selfimprovement.util;

import rkostiuk.selfimprovement.repository.filter.FilterCriteria;
import rkostiuk.selfimprovement.repository.filter.FilterOperation;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
                .map(RegexCriteriaQueryParser::matchesExpressionOrElseThrow)
                .map(RegexCriteriaQueryParser::buildCriteria)
                .toList();
    }

    private static Matcher matchesExpressionOrElseThrow(String expression) {
        Matcher matcher = CRITERIA_EXPRESSION_PATTERN.matcher(expression);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid query expression: '" + expression + "'");
        }
        return matcher;
    }

    private static FilterCriteria buildCriteria(Matcher matcher) {
        String valueString = matcher.group(3);
        Object value = parseIfNumber(valueString)
                .map(o -> (Object) o)
                .orElse(valueString);
        return new FilterCriteria(matcher.group(1), FilterOperation.fromCode(matcher.group(2)), value);
    }

    private static Optional<? extends Number> parseIfNumber(String value) {
        try {
            return Optional.of(Double.parseDouble(value));
        } catch (NumberFormatException e1) {
            try {
                return Optional.of(Long.parseLong(value));
            } catch (NumberFormatException e2) {
                return Optional.empty();
            }
        }
    }

}
