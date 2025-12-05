package aoc2025.day5;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

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
            if (ranges.stream().anyMatch(r -> r.contains(id))) {
                star1sol++;
            }
        }
        return Long.toString(star1sol);
    }

    public String star2() {
        Set<ParsedLine.Range> allRanges = new TreeSet<>();
        for (ParsedLine.Range newRange: ranges) {
            Optional<ParsedLine.Range> rangeContainingStart = allRanges.stream().filter(r -> r.contains(newRange.start())).findFirst();
            Optional<ParsedLine.Range> rangeContainingEnd = allRanges.stream().filter(r -> r.contains(newRange.end())).findFirst();
            if (rangeContainingStart.isEmpty() && rangeContainingEnd.isEmpty()) {
                allRanges.removeIf(r -> newRange.contains(r.start()) && newRange.contains(r.end()));
                allRanges.add(newRange);
            } else if (rangeContainingStart.isPresent() && rangeContainingEnd.isEmpty()) {
                ParsedLine.Range rangeToExtend = rangeContainingStart.get();
                ParsedLine.Range extendedRange = new ParsedLine.Range(rangeToExtend.start(), newRange.end());
                allRanges.removeIf(r -> extendedRange.contains(r.start()) && extendedRange.contains(r.end()));
                allRanges.add(extendedRange);
            } else if (rangeContainingStart.isEmpty()) {
                ParsedLine.Range rangeToExtend = rangeContainingEnd.get();
                ParsedLine.Range extendedRange = new ParsedLine.Range(newRange.start(), rangeToExtend.end());
                allRanges.removeIf(r -> extendedRange.contains(r.start()) && extendedRange.contains(r.end()));
                allRanges.add(extendedRange);
            } else { // both present
                if (rangeContainingStart.get().equals(rangeContainingEnd.get())) {
                    continue; // range already contained
                } else {
                    // join ranges
                    ParsedLine.Range extendedRange = new ParsedLine.Range(rangeContainingStart.get().start(), rangeContainingEnd.get().end());
                    allRanges.removeIf(r -> extendedRange.contains(r.start()) && extendedRange.contains(r.end()));
                    allRanges.add(extendedRange);
                }
            }
        }
        return Long.toString(allRanges.stream().mapToLong(ParsedLine.Range::length).sum());
    }
}
