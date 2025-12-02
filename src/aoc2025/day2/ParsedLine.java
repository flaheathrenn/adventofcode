package aoc2025.day2;

import java.util.ArrayList;
import java.util.List;

public class ParsedLine {

    // State
    List<Range> ranges;

    // Parsing
    public ParsedLine(String line) {
        ranges = new ArrayList<>();
        for (String subline : line.split(",")) {
            String[] splitSubline = subline.split("-");
            ranges.add(new Range(splitSubline[0], splitSubline[1]));
        }
    }

    public record Range(String start, String end) {
    }

}