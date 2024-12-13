package aoc2024.day13;

public class ParsedLine {

    // State
    public static enum LineType {
        BLANK, ABUTT, BBUTT, PRIZE;
    }

    LineType type;
    long xVal;
    long yVal;

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
        xVal = Long.parseLong(splitLine[1]);
        yVal = Long.parseLong(splitLine[3]);
        if (type == LineType.PRIZE) { // comment this if out to return to star 1
            xVal += 10000000000000L;
            yVal += 10000000000000L;
        }
    }

}