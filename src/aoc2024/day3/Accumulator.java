package aoc2024.day3;

public class Accumulator {
    // State
    long result = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        result += parsedLine.accumulator;
        return this;
    }

    // Extract solution
    public String star1() {
        return Long.toString(result);
    }
}
