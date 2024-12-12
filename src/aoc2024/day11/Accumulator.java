package aoc2024.day11;

public class Accumulator {
    // State
    StoneDirectory stones = new StoneDirectory();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        for (long stoneId : parsedLine.stones) {
            stones.addStone(stoneId, 1);
        }
        return this;
    }

    // Extract solution
    public String star(int blinks) {
        for (int i = 0; i < blinks; i++) {
            stones = stones.blink();
        }
        System.out.println(stones);
        return Long.toString(stones.size());
    }
}
