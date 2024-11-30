package aoc2023.day12;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedLine {

    // State
    String pattern;
    long combinations = 0;


    // Parsing
    public ParsedLine(String line) {
        String[] splitLine = line.split(" ");
        pattern = splitLine[0].replaceAll("\\.+", ".");
        String groupDefinitionString = splitLine[1];

        // Star 2 - unfold
        pattern = pattern + ("?" + pattern).repeat(4);
        groupDefinitionString = groupDefinitionString + ("," + groupDefinitionString).repeat(4);

        List<Integer> groupDefinition = Arrays.stream(groupDefinitionString.split(",")).map(Integer::valueOf).toList();
        combinations = calculateCombinationsWithMemo(pattern, groupDefinition);
    }

    private long calculateCombinationsWithMemo(String pattern, List<Integer> groupDefinition) {
        Optional<Long> memo = Memo.lookup(pattern, groupDefinition);
        if (memo.isPresent()) {
            return memo.get();
        }
        long result = calculateCombinations(pattern, groupDefinition);
        Memo.store(pattern, groupDefinition, result);
        return result;
    }

    private long calculateCombinations(String pattern, List<Integer> groupDefinition) {
        // Base cases:
        // no more groups
        if (groupDefinition.isEmpty()) {
            // one valid combination as long as there's not more #s to be found
            return pattern.indexOf("#") == -1 ? 1L : 0L;
        }
        // no more pattern (but some groups still remain)
        if (pattern.isBlank()) {
            // no valid combinations
            return 0L;
        }

        // Non-base cases:
        // Pattern starts with a dot: ignore dot
        if (pattern.startsWith(".")) {
            return calculateCombinationsWithMemo(pattern.substring(1), groupDefinition);
        }

        // Remainder of pattern is a single group: check group
        Pattern onlyGroup = Pattern.compile("^#+$");
        Matcher onlyGroupMatcher = onlyGroup.matcher(pattern);
        if (onlyGroupMatcher.find()) {
            int groupSize = onlyGroupMatcher.group().length();
            return List.of(groupSize).equals(groupDefinition) ? 1L : 0L;
        }

        // Pattern starts with a group of known length: check group, remove, calculate on substring
        Pattern openingGroup = Pattern.compile("^#+\\.");
        Matcher openingGroupMatcher = openingGroup.matcher(pattern);
        if (openingGroupMatcher.find()) {
            int groupSize = openingGroupMatcher.group().length() - 1; // remove closing dot
            if (groupSize != groupDefinition.get(0)) {
                return 0L; // invalid, no combinations
            }
            List<Integer> newGroupDefinition = List.copyOf(groupDefinition.subList(1, groupDefinition.size()));
            return calculateCombinationsWithMemo(pattern.substring(groupSize), newGroupDefinition);
        }
        
        // Pattern starts with a group of unknown length or a ?: recurse
        String dotCase = pattern.replaceFirst("\\?", ".");
        String hashCase = pattern.replaceFirst("\\?", "#");
        return calculateCombinationsWithMemo(dotCase, groupDefinition) + calculateCombinationsWithMemo(hashCase, groupDefinition);
    }

}