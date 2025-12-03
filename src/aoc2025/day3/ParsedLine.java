package aoc2025.day3;

import java.util.Arrays;

public class ParsedLine {

    // State
    int[] bank;

    // Parsing
    public ParsedLine(String line) {
        bank = Arrays.stream(line.split("")).mapToInt(Integer::parseInt).toArray();
    }

}