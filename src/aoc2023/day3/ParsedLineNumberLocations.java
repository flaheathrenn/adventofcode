package aoc2023.day3;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedLineNumberLocations {

    // State
    Set<NumberAndLocation> numberLocations;

    // Parsing
    public ParsedLineNumberLocations(String line) {
        numberLocations = new HashSet<NumberAndLocation>();
        Matcher numberMatcher = Pattern.compile("\\d+").matcher(line);
        while (numberMatcher.find()) {
            numberLocations.add(new NumberAndLocation(
                Integer.parseInt(numberMatcher.group()),
                numberMatcher.start() + 1,
                numberMatcher.end() + 1));
        }
    }

    public static record NumberAndLocation(int value, int start, int end) {}

}