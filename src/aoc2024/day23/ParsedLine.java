package aoc2024.day23;

public class ParsedLine {

    // State
    String[] connection;

    // Parsing
    public ParsedLine(String line) {
        connection = line.split("-");
    }

}