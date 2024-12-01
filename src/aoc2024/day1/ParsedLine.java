package aoc2024.day1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedLine {

    // State
    long left;
    long right;


    // Parsing
    public ParsedLine(String line) {
        Matcher lineMatcher = Pattern.compile("^(\\d+)\\s+(\\d+)$").matcher(line);
        lineMatcher.matches();
        left = Long.parseLong(lineMatcher.group(1));
        right = Long.parseLong(lineMatcher.group(2));
        // System.out.println(String.format("Parsed %s to %d and %d", line, left, right));
    }

}