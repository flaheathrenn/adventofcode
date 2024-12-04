package aoc2024.day4;

public class ParsedLine {

    // State
    String[] row;

    // Parsing
    public ParsedLine(String line) {
        row = line.split("");
    }

}