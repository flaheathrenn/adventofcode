package aoc2025.day7;

import java.util.HashSet;
import java.util.Set;

public class Accumulator {
    // State
    int star1sol = 0;
    Set<Integer> beamPositions = new HashSet<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (parsedLine.sPosition != -1) {
            beamPositions.add(parsedLine.sPosition);
        }
        for (int splitterPosition : parsedLine.splitterPositions) {
            if (beamPositions.contains(splitterPosition)) {
                star1sol++;
                beamPositions.add(splitterPosition - 1);
                beamPositions.add(splitterPosition + 1);
                beamPositions.remove(splitterPosition);
            }
        }
        return this;
    }

    // Extract solution
    public String star1() {
        return Integer.toString(star1sol);
    }
}
