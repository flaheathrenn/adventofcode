package day3;

public class Accumulator {
    // State
    int currentLineNumber = 1;
    int currentTotal = 0;

    // Update state from parsed line
    public Accumulator update(int lineTotal) {
        currentLineNumber++;
        currentTotal += lineTotal;
        return this;
    }

    // Extract solution
    public String star1() {
        return String.valueOf(currentTotal);
    }
}
