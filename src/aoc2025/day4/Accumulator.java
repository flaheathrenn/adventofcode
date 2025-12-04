package aoc2025.day4;

import global.GridUtils;

import java.util.ArrayList;
import java.util.List;

public class Accumulator {
    private static final String ROLL_OF_PAPER = "@";

    // State
    List<String[]> gridList = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        gridList.add(parsedLine.line);
        return this;
    }

    // Extract solution
    public String star1() {
        long accessibleRollsCount = 0;
        String[][] grid = gridList.toArray(new String[gridList.size()][gridList.getFirst().length]);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                GridUtils.GridCoordinate gc = new GridUtils.GridCoordinate(i, j);
                if (!ROLL_OF_PAPER.equals(gc.read(grid))) {
                    continue;
                }
                int adjacentRollCount = 0;
                for (GridUtils.DirectionFullCompass d : GridUtils.DirectionFullCompass.values()) {
                    if (ROLL_OF_PAPER.equals(gc.step(d).read(grid))) {
                        adjacentRollCount++;
                    }
                    if (adjacentRollCount > 3) {
                        break;
                    }
                }
                if (adjacentRollCount < 4) {
                    accessibleRollsCount++;
                }
            }
        }
        return Long.toString(accessibleRollsCount);
    }

    public String star2() {
        long previousAccessibleRollsCount, accessibleRollsCount = 0;
        String[][] grid = gridList.toArray(new String[gridList.size()][gridList.getFirst().length]);
        do {
            previousAccessibleRollsCount = accessibleRollsCount;
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    GridUtils.GridCoordinate gc = new GridUtils.GridCoordinate(i, j);
                    if (!ROLL_OF_PAPER.equals(gc.read(grid))) {
                        continue;
                    }
                    int adjacentRollCount = 0;
                    for (GridUtils.DirectionFullCompass d : GridUtils.DirectionFullCompass.values()) {
                        if (ROLL_OF_PAPER.equals(gc.step(d).read(grid))) {
                            adjacentRollCount++;
                        }
                        if (adjacentRollCount > 3) {
                            break;
                        }
                    }
                    if (adjacentRollCount < 4) {
                        accessibleRollsCount++;
                        grid[gc.i()][gc.j()] = ".";
                    }
                }
            }
        } while (previousAccessibleRollsCount != accessibleRollsCount);
        return Long.toString(accessibleRollsCount);
    }
}
