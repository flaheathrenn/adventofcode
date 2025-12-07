package aoc2025.day7;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Accumulator {
    // State
    int star1sol = 0;
    Map<Integer, Long> beams = new HashMap<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (parsedLine.sPosition != -1) {
            beams.put(parsedLine.sPosition, 1L);
        }
        for (int splitterPosition : parsedLine.splitterPositions) {
            if (beams.containsKey(splitterPosition)) {
                star1sol++;
                beams.put(splitterPosition - 1, beams.getOrDefault(splitterPosition - 1, 0L) + beams.get(splitterPosition));
                beams.put(splitterPosition + 1, beams.getOrDefault(splitterPosition + 1, 0L) + beams.get(splitterPosition));
                beams.remove(splitterPosition);
            }
        }
        return this;
    }

    // Extract solution
    public String star1() {
        return Integer.toString(star1sol);
    }

    public String star2() {
        return beams.values().stream().reduce(Long::sum).map(String::valueOf).orElse("ERROR");
    }
}
