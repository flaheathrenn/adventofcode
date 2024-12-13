package aoc2024.day13;

public class ParsedLine {

    // State
    public static enum LineType {
        BLANK, ABUTT, BBUTT, PRIZE;
    }

    LineType type;
    int xVal;
    int yVal;

    // Parsing
    public ParsedLine(String line) {
        if (line.isEmpty()) {
            type = LineType.BLANK;
            return;
        }
        if (line.startsWith("Prize")) {
            type = LineType.PRIZE;
        } else if (line.startsWith("Button A")) {
            type = LineType.ABUTT;
        } else if (line.startsWith("Button B")) {
            type = LineType.BBUTT;
        }
        String[] splitLine = line.split("[=+,]");
        xVal = Integer.parseInt(splitLine[1]);
        yVal = Integer.parseInt(splitLine[3]);
    }

}