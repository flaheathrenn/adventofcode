package day12;

public class Accumulator {
    // State
    int acc = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        acc += parsedLine.combinations;
        return this;
    }

    // Extract solution
    public String star1() {
        return String.valueOf(acc);
    }
}
