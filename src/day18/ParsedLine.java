package day18;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import day18.GridUtils.Direction;

public class ParsedLine {

    // State
    final Direction direction;
    final int distance;
    final String colourCode;

    // Parsing
    private final Pattern LINE_PATTERN = Pattern.compile("([UDLR]) (\\d+) \\((#.+)\\)");

    public ParsedLine(String line) {
        // Example: R 6 (#70c710)
        Matcher m = LINE_PATTERN.matcher(line);
        m.find();
        this.direction = switch (m.group(1)) {
            case "U" -> Direction.UP;
            case "L" -> Direction.LEFT;
            case "R" -> Direction.RIGHT;
            case "D" -> Direction.DOWN;
            default -> throw new IllegalArgumentException();
        };
        this.distance = Integer.parseInt(m.group(2));
        this.colourCode = m.group(3);
    }

}