package aoc2023.day15;

public class Accumulator {
    // State
    private long acc;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        acc = parsedLine.totalFocusingPower;
        return this;
    }

    // Extract solution
    public String star1() {
        return String.valueOf(acc);
    }
}
