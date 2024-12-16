package aoc2023.day17;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Accumulator {
    // State
    List<int[]> gridRows = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        gridRows.add(parsedLine.values);
        return this;
    }

    // Extract solution
    public String star1() {
        int[][] grid = gridRows.toArray(new int[gridRows.size()][gridRows.get(0).length]);

        // print for debugging
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
        System.out.println();

        // Calculate upper bound
        // GridCoordinate on = new GridCoordinate(0, 0);
        // GridCoordinate end = GridCoordinate.bottomRight(grid);
        // Direction direction = Direction.RIGHT;
        int upperBound = 0;
        // while (!on.equals(end)) {
        //     upperBound += on.readFromGrid(grid);
        //     on = on.advance(direction);
        //     direction = direction == Direction.RIGHT ? Direction.DOWN : Direction.RIGHT;
        // }
        // System.out.println("Calculated an upper bound of " + upperBound);

        GridCoordinate targetCoordinate = new GridCoordinate(0, 0);
        Map<GridCoordinate, SpaceInfo> shortestPathsDirectory = new HashMap<>();
        List<GridCoordinate> visited = new ArrayList<>();
        int shortestPath = findShortestPath(grid, targetCoordinate, shortestPathsDirectory, new Run(null, 0), visited, upperBound);

        // print for debugging
        System.out.println();
        String[][] resultGrid = new String[grid.length][grid[0].length];
        for (int i = 0; i < resultGrid.length; i++) {
            for (int j = 0; j < resultGrid[i].length; j++) {
                resultGrid[i][j] = ".";
            }
        }
        GridCoordinate tracingFrom = new GridCoordinate(0, 0);
        GridCoordinate target = new GridCoordinate(grid.length - 1, grid[0].length - 1);
        Run tracingRun = new Run(null, 0);
        while (!tracingFrom.equals(target)) {
            // System.out.println("Tracing shortest path from " + tracingFrom + ", current
            // run " + tracingRun + "...");
            PathInfo nextStep = shortestPathsDirectory.get(tracingFrom).findShortestPathWhenEnteredByRun(tracingRun)
                    .get();
            tracingRun = tracingRun.compose(nextStep.direction);
            tracingFrom = tracingFrom.advance(nextStep.direction);
            resultGrid[tracingFrom.row][tracingFrom.column] = nextStep.direction.asciiArt();
        }
        for (int i = 0; i < resultGrid.length; i++) {
            for (int j = 0; j < resultGrid[i].length; j++) {
                System.out.print(resultGrid[i][j]);
            }
            System.out.println();
        }

        return String.valueOf(shortestPath - grid[0][0]);
    }

    /**
     * Calculate the shortest path to a target grid coordinate from coordinate
     * (0,0),
     * with the constraint that the path must be able to have a run added to the end
     * and remain
     * legal.
     * Returns the path length
     */
    private int findShortestPath(int[][] grid, GridCoordinate currentCoordinate,
            Map<GridCoordinate, SpaceInfo> shortestPathsDirectory, Run currentRun, List<GridCoordinate> visited, int upperBound) {
        System.out.println("Looking for shortest path from " + currentCoordinate + " after " + currentRun);
        System.out.println("Path length so far: " + upperBound);
        // try {
        // Thread.sleep(1000L);
        // } catch (InterruptedException e) {
        // // noop;
        // }
        // if over upper bound, this is a bad path, return immediately
        // if (upperBound < 0) {
        //     System.out.println("Upper bound exceeded, aborting");
        //     return Integer.MAX_VALUE;
        // }

        // if at end, done
        GridCoordinate end = GridCoordinate.bottomRight(grid);
        if (end.equals(currentCoordinate)) {
            return currentCoordinate.readFromGrid(grid);
        }
        // look up in directory
        if (shortestPathsDirectory.containsKey(currentCoordinate)) {
            Optional<PathInfo> recordedShortestPath = shortestPathsDirectory.get(currentCoordinate)
                    .findShortestPathWhenEnteredByRun(currentRun);
            if (recordedShortestPath.isPresent()) {
                System.out.println("Found useable shortest path from " + currentCoordinate +
                " given run " + currentRun
                + ", length " + recordedShortestPath.get().length);
                return recordedShortestPath.get().length;
            } else {
                System.out.println("No useable shortest path from " + currentCoordinate + " given run " + currentRun);
            }
        }
        visited.add(currentCoordinate);
        // otherwise, try all possible paths
        List<Direction> possibleDirections = new ArrayList<>(List.of(Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP));
        if (currentRun.direction != null) {
            possibleDirections.remove(currentRun.direction.opposite());
            if (currentRun.length == 3) {
                possibleDirections.remove(currentRun.direction);
            }
        }
        int thisTileValue = currentCoordinate.readFromGrid(grid);
        int currentShortestPath = Integer.MAX_VALUE;
        Direction directionToMove = null;
        for (Direction direction : possibleDirections) {
            GridCoordinate newCoordinate = currentCoordinate.advance(direction);
            if (!newCoordinate.validOnGrid(grid)) {
                continue; // don't go that way
            }
            if (visited.contains(newCoordinate)) {
                continue; // don't go that way
            }
            Run newRun = currentRun.compose(direction);
            int shortestPathLengthFromThere = findShortestPath(grid, newCoordinate, shortestPathsDirectory, newRun,
                    new ArrayList<>(visited), upperBound + thisTileValue);
            if (shortestPathLengthFromThere < currentShortestPath) {
                currentShortestPath = shortestPathLengthFromThere;
                directionToMove = direction;
            }
        }
        if (currentShortestPath == Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        int newShortestPathLength = currentShortestPath + thisTileValue;
        if (!shortestPathsDirectory.containsKey(currentCoordinate)) {
            shortestPathsDirectory.put(currentCoordinate, new SpaceInfo());
        }
        shortestPathsDirectory.get(currentCoordinate).shortestPaths
                .add(new PathInfo(newShortestPathLength, currentRun, directionToMove));
        System.out.println("Recorded shortest path from " + currentCoordinate + " as " + newShortestPathLength
        + " when entered from " + currentRun);
        return newShortestPathLength;
    }

    private static record Run(Direction direction, int length) {
        Run compose(Direction newDirection) {
            if (direction != newDirection) {
                return new Run(newDirection, 1);
            } else {
                return new Run(direction, length + 1);
            }
        }
    }

    private static record PathInfo(int length, Run run, Direction direction) implements Comparable<PathInfo> {

        @Override
        public int compareTo(PathInfo o) {
            return Integer.compare(length, o.length);
        }

    }

    private static class SpaceInfo {
        // Returns the shortest path found from this space when entered by run Run
        Set<PathInfo> shortestPaths = new HashSet<PathInfo>();

        Optional<PathInfo> findShortestPathWhenEnteredByRun(Run run) {
            // System.out.println("Raw data: " + shortestPaths);
            if (shortestPaths.isEmpty()) {
                return Optional.empty();
            }
            if (run.direction == null) {
                return shortestPaths.stream()
                        .min(PathInfo::compareTo);
            }
            return shortestPaths.stream()
                    .filter(pathInfo -> pathInfo.run.direction == run.direction)
                    .filter(pathInfo -> pathInfo.run.length == run.length)
                    .min(PathInfo::compareTo);
        }
    }

    static enum Direction {
        UP, LEFT, DOWN, RIGHT;

        Direction opposite() {
            return switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }

        String asciiArt() {
            return switch (this) {
                case UP -> "^";
                case DOWN -> "v";
                case LEFT -> "<";
                case RIGHT -> ">";
            };
        }
    }

    private static record GridCoordinate(int row, int column) {
        GridCoordinate advance(Direction direction) {
            switch (direction) {
                case LEFT: {
                    return new GridCoordinate(row, column - 1);
                }
                case RIGHT: {
                    return new GridCoordinate(row, column + 1);
                }
                case UP: {
                    return new GridCoordinate(row - 1, column);
                }
                case DOWN: {
                    return new GridCoordinate(row + 1, column);
                }
                default: {
                    throw new IllegalArgumentException();
                }
            }
        }

        int readFromGrid(int[][] grid) {
            return grid[row][column];
        }

        static GridCoordinate bottomRight(int[][] grid) {
            if (grid.length == 0) {
                throw new IllegalArgumentException();
            }
            return new GridCoordinate(grid.length - 1, grid[0].length - 1);
        }

        boolean validOnGrid(int[][] grid) {
            int maxRow = grid.length;
            int maxColumn = grid[0].length;
            return row >= 0 && column >= 0 && row < maxRow && column < maxColumn;
        }
    }

}
