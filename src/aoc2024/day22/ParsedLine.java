package aoc2024.day22;

public class ParsedLine {

    // State
    long seed;

    // Parsing
    public ParsedLine(String line) {
        seed = Long.parseLong(line);
    }

}