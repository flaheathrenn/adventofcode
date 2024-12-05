package aoc2024.day5;

import java.util.Arrays;
import java.util.List;

public class ParsedLine {

    // State
    boolean isRule = false;
    boolean isPages = false;
    Rule rule;
    List<String> pages;

    // Parsing
    public ParsedLine(String line) {
        if (line.contains("|")) {
            isRule = true;
            String[] splitLine = line.split("\\|");
            rule = new Rule(splitLine[0], splitLine[1]);
        } else if (line.contains(",")) {
            isPages = true;
            pages = List.of(line.split(","));
        }
    }

    public static record Rule(String left, String right) {
    }

}