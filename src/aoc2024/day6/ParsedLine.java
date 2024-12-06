package aoc2024.day6;

public class ParsedLine {

    // State
    String[] row;

    // Parsing
    public ParsedLine(String line) {
        row = line.split("");
    }

}