package aoc2023.day16;

public class ParsedLine {

    // State
    String[] row;

    // Parsing
    public ParsedLine(String line) {
        row = line.split("");
    }

}