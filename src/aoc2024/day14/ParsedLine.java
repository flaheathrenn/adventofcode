package aoc2024.day14;

public class ParsedLine {

    // State
    long px;
    long py;
    long vx;
    long vy;

    // Parsing
    public ParsedLine(String line) {
        // p=0,4 v=3,-3
        String[] splitLine = line.split("[=, ]");
        px = Long.parseLong(splitLine[1]);
        py = Long.parseLong(splitLine[2]);
        vx = Long.parseLong(splitLine[4]);
        vy = Long.parseLong(splitLine[5]);
    }

}