package aoc2024.day18;

import global.GridUtils.GridCoordinate;

public class ParsedLine {

    // State
    GridCoordinate corrupted;

    // Parsing
    public ParsedLine(String line) {
        String[] splitLine = line.split(",");
        corrupted = new GridCoordinate(Integer.parseInt(splitLine[0]), Integer.parseInt(splitLine[1]));
    }

}