package aoc2025.day11;

public class ParsedLine {
    // Example line:
    // ccc: ddd eee fff

    // State
    String input;
    String[] outputs;

    // Parsing
    public ParsedLine(String line) {
        String[] splitLine = line.split(": ");
        input = splitLine[0];
        outputs = splitLine[1].split(" ");
    }

}