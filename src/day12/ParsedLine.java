package day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedLine {

    // State
    String pattern;
    List<Integer> groupDefinition;
    long combinations = 0;


    // Parsing
    public ParsedLine(String line) {
        String[] splitLine = line.split(" ");
        pattern = splitLine[0].replaceAll("\\.+", ".");
        String groupDefinitionString = splitLine[1];

        // Star 2 - unfold
        pattern = pattern + ("?" + pattern).repeat(4);
        groupDefinitionString = groupDefinitionString + ("," + groupDefinitionString).repeat(4);

        groupDefinition = Arrays.stream(groupDefinitionString.split(",")).map(Integer::valueOf).toList();
        combinations = calculateCombinations(pattern);
    }

    private long calculateCombinations(String pattern) {
        System.out.println("Testing " + pattern);
        if (pattern.indexOf("?") == -1) {
            return matchesGroupDefinition(pattern, groupDefinition) ? 1L : 0L;
        }
        String completedPrefix = pattern.substring(0, pattern.indexOf("?"));
        if (!canMatchGroupDefinition(completedPrefix, groupDefinition)) {
            return 0L;
        }
        String dotCase = pattern.replaceFirst("\\?", ".");
        String hashCase = pattern.replaceFirst("\\?", "#");
        return calculateCombinations(dotCase) + calculateCombinations(hashCase);
    }

    private boolean canMatchGroupDefinition(String patternSoFar, List<Integer> groupDefinition) {
        List<Integer> patternDefinitionSoFar = new ArrayList<>(countPattern(patternSoFar));
        if (patternDefinitionSoFar.isEmpty()) {
            return true;
        }
        if (patternSoFar.endsWith("#")) {
            // if the final character before the first ? is a #, final group size is not certain yet
            patternDefinitionSoFar.remove(patternDefinitionSoFar.size() - 1);
        }
        return Collections.indexOfSubList(groupDefinition, patternDefinitionSoFar) == 0;
    }

    private boolean matchesGroupDefinition(String pattern, List<Integer> groupDefinition) {
        return countPattern(pattern).equals(groupDefinition);
    }

    private List<Integer> countPattern(String pattern) {
        Matcher matcher = Pattern.compile("#+").matcher(pattern);
        matcher.matches();
        List<Integer> patternDefinition = new ArrayList<>();
        while (matcher.find()) {
            patternDefinition.add(matcher.group().length());
        }
        return patternDefinition;
    }

}