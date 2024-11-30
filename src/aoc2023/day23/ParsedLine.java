package aoc2023.day23;

public class ParsedLine {

    // State
    String[] row;


    // Parsing
    public ParsedLine(String line) {
        row = line.split("");
    }

}