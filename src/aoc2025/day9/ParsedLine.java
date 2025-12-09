package aoc2025.day9;

public class ParsedLine {

    // State
    Corner corner;

    // Parsing
    public ParsedLine(String line) {
        String[] splitLine = line.split(",");
        corner = new Corner(Long.parseLong(splitLine[0]), Long.parseLong(splitLine[1]));
    }

    public record Corner(long x, long y) {}

}