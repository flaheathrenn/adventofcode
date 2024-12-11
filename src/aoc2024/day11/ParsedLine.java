package aoc2024.day11;

import java.util.Arrays;

public class ParsedLine {
    // State
    long[] stones;

    // Parsing
    public ParsedLine(String line) {
        stones = Arrays.stream(line.split(" ")).mapToLong(Long::parseLong).toArray();
    }

}