package aoc2025.day5;

import java.util.Optional;

public class ParsedLine {

    // State
    public Optional<Range> range = Optional.empty();
    public Optional<Long> id = Optional.empty();

    // Parsing
    public ParsedLine(String line) {
        if ("".equals(line)) {
            return;
        }
        String[] splitLine = line.split("-");
        if (splitLine.length == 2) {
            range = Optional.of(new Range(Long.parseLong(splitLine[0]), Long.parseLong(splitLine[1])));
        } else if (splitLine.length == 1) {
            id = Optional.of(Long.parseLong(splitLine[0]));
        } else {
            throw new IllegalStateException();
        }
    }

    public record Range(long start, long end) { }

}