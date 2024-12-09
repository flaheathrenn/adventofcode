package aoc2024.day9;

public class Accumulator {
    // State
    long star1checksum = 0;
    long star2checksum = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        star1checksum += parsedLine.star1checksum;
        star2checksum += parsedLine.star2checksum;
        return this;
    }

    // Extract solution
    public String star1() {
        return Long.toString(star1checksum);
    }

    public String star2() {
        return Long.toString(star2checksum);
    }
}
