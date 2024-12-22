package aoc2023.day25;

public class ParsedLine {

    // State
        String[] items;

    // Parsing
    public ParsedLine(String line) {
        items = line.split(":? ");
    }

}