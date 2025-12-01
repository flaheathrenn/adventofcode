package aoc2025.day1;

public class ParsedLine {

    // State
    int value;

    // Parsing
    public ParsedLine(String line) {
        switch (line.substring(0,1)) {
            case "R":
                value = Integer.parseInt(line.substring(1));
                break;
            case "L":
                value = -Integer.parseInt(line.substring(1));
                break;
            default:
                throw new IllegalStateException();
        }
    }

}