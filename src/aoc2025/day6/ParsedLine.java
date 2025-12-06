package aoc2025.day6;

public class ParsedLine {

    // State
    String[] contents;

    // Parsing
    public ParsedLine(String line) {
        contents = line.trim().split(" +", 0);
    }

}