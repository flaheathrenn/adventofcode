package aoc2023.day3;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedLineSymbolLocations {

    // State
    public Set<Integer> symbolLocations;

    // Parsing
    public ParsedLineSymbolLocations(String line) {
        this.symbolLocations = new HashSet<>();
        Matcher symbolMatcher = Pattern.compile("\\*").matcher(line);
        while (symbolMatcher.find()) {
            symbolLocations.add(symbolMatcher.start() + 1); // convert zero-index to one-index
        }
    }

}