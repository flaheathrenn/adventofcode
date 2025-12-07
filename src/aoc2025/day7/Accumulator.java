package aoc2025.day7;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Accumulator {
    // State
    int star1sol = 0;
    Set<Integer> beamPositions = new HashSet<>();
    Map<Integer, Long> beams = new HashMap<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (parsedLine.sPosition != -1) {
            beamPositions.add(parsedLine.sPosition);
            beams.put(parsedLine.sPosition, 1L);
        }
        for (int splitterPosition : parsedLine.splitterPositions) {
            if (beamPositions.contains(splitterPosition)) {
                star1sol++;
                beamPositions.add(splitterPosition - 1);
                beamPositions.add(splitterPosition + 1);
                beamPositions.remove(splitterPosition);
            }
            if (beams.containsKey(splitterPosition)) {
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
