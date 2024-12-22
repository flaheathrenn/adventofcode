package aoc2024.day22;

public class Accumulator {
    // State
    long sum = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        long seed = parsedLine.seed;
        for (int i = 0; i < 2000; i++) {
            seed = iterate(seed);
        }
        sum += seed;
        return this;
    }

    // Extract solution
    public String star1() {
        return Long.toString(sum);
    }

    long iterate(long seed) {
        long sixfour = seed * 64;
        seed = seed ^ sixfour;
        seed = seed % 16777216L;
        long threetwo = seed / 32;
        seed = seed ^ threetwo;
        seed = seed % 16777216L;
        long twooh = seed * 2048;
        seed = seed ^ twooh;
        return seed % 16777216L;
    }
}
