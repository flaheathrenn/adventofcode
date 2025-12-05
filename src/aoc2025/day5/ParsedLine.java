package aoc2025.day5;

import java.util.Comparator;
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

    public record Range(long start, long end) implements Comparable<Range> {
        public boolean contains(long id) {
            return start <= id && end >= id;
        }

        @Override
        public int compareTo(Range o) {
            return Long.compare(this.start, o.start);
        }

        public long length() {
            return this.end - this.start + 1;
        }
    }

}