package aoc2024.day21;

public class ParsedLine {

    // State
    int numericPart;
    String line;

    // Parsing
    public ParsedLine(String line) {
        this.numericPart = Integer.parseInt(line.split(" ")[0]);
        this.line = line.split(" ")[1];
        }

}