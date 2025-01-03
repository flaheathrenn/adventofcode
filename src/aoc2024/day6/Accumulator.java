package aoc2024.day6;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aoc2024.day6.GridUtils.Direction;
import aoc2024.day6.GridUtils.GridCoordinate;

public class Accumulator {
    // State
    List<String[]> rows = new ArrayList<>();
    Set<GridCoordinate> originalPath = new HashSet<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        rows.add(parsedLine.row);
        return this;
    }

    // Extract solution
    public String star1() {
        String[][] grid = GridUtils.deepCopyOf(rows.toArray(new String[0][0]));
        GridCoordinate currentLocation = GridUtils.find(grid, "^");
        Direction currentDirection = Direction.UP;
        while (currentLocation.isWithin(grid)) {
            originalPath.add(currentLocation);
            GridCoordinate nextLocation = currentLocation.step(currentDirection);
            if (nextLocation.read(grid).equals("#")) {
                currentDirection = currentDirection.rotate(); // hit an obstacle
                continue;
            }
            currentLocation.write(grid, "X");
            currentLocation = nextLocation;
        }
        // GridUtils.prettyPrint(grid);
        return String.valueOf(GridUtils.count(grid, "X"));
    }

    public String star2() {
        long count = 0;
        String[][] grid = GridUtils.deepCopyOf(rows.toArray(new String[0][0]));
        GridCoordinate startLocation = GridUtils.find(grid, "^");
        originalPath.remove(startLocation);
        for (GridCoordinate obstacleLocation : originalPath) {
            String[][] testGrid = GridUtils.deepCopyOf(grid);
            obstacleLocation.write(testGrid, "#");
            startLocation.write(testGrid, "."); // to avoid finding the starting ^
                                                        // being treated as a loop
            if (doesLoop(testGrid, startLocation)) {
                count++;
            }
        }
        return String.valueOf(count);
    }

    private boolean doesLoop(String[][] grid, GridCoordinate startLocation) {
        GridCoordinate currentLocation = startLocation;
        Direction currentDirection = Direction.UP;
        while (currentLocation.isWithin(grid)) {
            GridCoordinate nextLocation = currentLocation.step(currentDirection);
            if (nextLocation.read(grid).equals("#")) {
                currentDirection = currentDirection.rotate(); // hit an obstacle
                continue;
            }
            if (currentLocation.read(grid).equals(currentDirection.marker())) {
                // loop detected!
                return true;
            }
            currentLocation.write(grid, currentDirection.marker());
            currentLocation = nextLocation;
        }
        return false; // guard has left grid
    }
}
