package aoc2025.day6;

import java.util.Arrays;

public class ParsedLine {

    // State
    String[] contents;
    String[] rawContents;

    // Parsing
    public ParsedLine(String line) {
        contents = line.trim().split(" +");
        rawContents = line.split("");
        System.out.println(Arrays.toString(rawContents));
    }

}