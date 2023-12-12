package day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedLine {

    // State
    String pattern;
    List<Integer> groupDefinition;
    int combinations = 0;


    // Parsing
    public ParsedLine(String line) {
        String[] splitLine = line.split(" ");
        pattern = splitLine[0].replaceAll("\\.+", ".");
        String groupDefinitionString = splitLine[1];

        // Star 2 - unfold
        // pattern = pattern + ("?" + pattern).repeat(4);
        // groupDefinitionString = groupDefinitionString + ("," + groupDefinitionString).repeat(4);

        groupDefinition = Arrays.stream(groupDefinitionString.split(",")).map(Integer::valueOf).toList();
        combinations = calculateCombinations(pattern);
    }

    private int calculateCombinations(String pattern) {
        if (pattern.indexOf("?") == -1) {
            return matchesGroupDefinition(pattern, groupDefinition) ? 1 : 0;
        }
        String dotCase = pattern.replaceFirst("\\?", ".");
        String hashCase = pattern.replaceFirst("\\?", "#");
        return calculateCombinations(dotCase) + calculateCombinations(hashCase);
    }

    private boolean matchesGroupDefinition(String pattern, List<Integer> groupDefinition) {
        Matcher matcher = Pattern.compile("#+").matcher(pattern);
        matcher.matches();
        List<Integer> patternDefinition = new ArrayList<>();
        while (matcher.find()) {
            patternDefinition.add(matcher.group().length());
        }
        return patternDefinition.equals(groupDefinition);
    }

}