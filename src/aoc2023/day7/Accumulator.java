package aoc2023.day7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Accumulator {
    // State
    List<ParsedLine> handsAndBids = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        handsAndBids.add(parsedLine);
        return this;
    }

    // Extract solution
    public String star1() {
        long totalWinnings = 0L;
        Collections.sort(handsAndBids);
        int size = handsAndBids.size();
        for (int i = 0; i < size; i++) {
            int rank = i + 1;
            System.out.println("Hand " + handsAndBids.get(i).hand + " is rank " + rank);
            totalWinnings += rank * handsAndBids.get(i).bid;
        }
        return String.valueOf(totalWinnings);
    }
}
