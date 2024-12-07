package aoc2024.day7;

import java.util.Arrays;
import java.util.List;

public class ParsedLine {

    // State
    long target;
    List<Long> elements;

    // Parsing
    public ParsedLine(String line) {
        String[] splitLine = line.split("[: ]");
        target = Long.parseLong(splitLine[0]);
        elements = Arrays.stream(splitLine).filter(s -> !s.isBlank()).map(Long::valueOf).toList();
        elements = elements.subList(1, elements.size());

    }

}