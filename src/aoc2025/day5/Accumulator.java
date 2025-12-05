package aoc2025.day5;

import java.util.HashSet;
import java.util.Set;

public class Accumulator {
    // State
    Set<ParsedLine.Range> ranges = new HashSet<>();
    Set<Long> ids = new HashSet<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        parsedLine.range.ifPresent(ranges::add);
        parsedLine.id.ifPresent(ids::add);
        return this;
    }

    // Extract solution
    public String star1() {
        long star1sol = 0;
        for (Long id : ids) {
            if (ranges.stream().anyMatch(r -> r.start() <= id && r.end() >= id)) {
                star1sol++;
            }
        }
        return Long.toString(star1sol);
    }
}
