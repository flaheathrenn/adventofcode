package aoc2024.day2;

public class Accumulator {
    // State
    long safeRecordCount = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        safeRecordCount += parsedLine.recordSafe ? 1 : 0;
        return this;
    }

    // Extract solution
    public String star1() {
        return Long.toString(safeRecordCount);
    }
}
