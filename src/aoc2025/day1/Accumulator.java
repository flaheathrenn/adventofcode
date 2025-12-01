package aoc2025.day1;

public class Accumulator {
    // State
    int pos = 50;
    int pw = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        pos += parsedLine.value;
        pos = ((pos % 100) + 100) % 100;
        if (pos == 0) {
            pw++;
        }
        return this;
    }

    // Extract solution
    public String star1() {
        return Integer.toString(pw);
    }
}
