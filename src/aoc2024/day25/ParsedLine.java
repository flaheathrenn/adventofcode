package aoc2024.day25;

public class ParsedLine {

    // State
    boolean isBlank = false;
    String line;

    // Parsing
    public ParsedLine(String line) {
        if (line.isEmpty()) {
            isBlank = true;
        }
        this.line = line;
    }

}