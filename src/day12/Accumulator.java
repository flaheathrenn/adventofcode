package day12;

public class Accumulator {
    // State
    long acc = 0;
    int lineNumber = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        acc += parsedLine.combinations;
        System.out.println("Processed line " + lineNumber++);
        return this;
    }

    // Extract solution
    public String star1() {
        return String.valueOf(acc);
    }
}
