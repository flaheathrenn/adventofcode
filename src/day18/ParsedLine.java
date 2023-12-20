package day18;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import day18.GridUtils.Direction;

public class ParsedLine {

    // State
    final Direction direction;
    final long distance;
    // final String colourCode;

    // Parsing
    // private final Pattern LINE_PATTERN = Pattern.compile("([UDLR]) (\\d+) \\((#.+)\\)");
    private final Pattern LINE_PATTERN = Pattern.compile("#(.{5})([0123])");

    public ParsedLine(String line) {
        // Example: R 6 (#70c710)
        Matcher m = LINE_PATTERN.matcher(line);
        m.find();
        this.direction = switch (m.group(2)) {
            case "0" -> Direction.RIGHT;
            case "1" -> Direction.DOWN;
            case "2" -> Direction.LEFT;
            case "3" -> Direction.UP;
            default -> throw new IllegalArgumentException();
        };
        this.distance = Long.parseLong(m.group(1), 16);
    }

}