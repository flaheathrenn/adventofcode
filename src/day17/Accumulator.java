package day17;

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

        GridCoordinate targetCoordinate = new GridCoordinate(grid.length - 1, grid[0].length - 1);
        Map<GridCoordinate, SpaceInfo> shortestPathsDirectory = new HashMap<>();
        List<GridCoordinate> visited = new ArrayList<>();
        int shortestPath = findShortestPathFromStart(grid, targetCoordinate, shortestPathsDirectory, new Run(null, 0), visited).orElse(Integer.MAX_VALUE);

        return String.valueOf(shortestPath);
    }

    /**
     * Calculate the shortest path to a target grid coordinate from coordinate
     * (0,0),
     * with the constraint that the path must be able to have a run added to the end
     * and remain
     * legal.
     * Returns the path length
     */
    private Optional<Integer> findShortestPathFromStart(int[][] grid, GridCoordinate targetCoordinate,
            Map<GridCoordinate, SpaceInfo> shortestPathsDirectory, Run run, List<GridCoordinate> visited) {
        System.out.println("Looking for shortest path to " + targetCoordinate.toString() + " that admits run " + run.toString());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            // noop;
        }
        if (new GridCoordinate(0, 0).equals(targetCoordinate)) {
            return Optional.of(0);
        }
        if (visited.contains(targetCoordinate)) {
            return Optional.empty();
        }
        visited.add(targetCoordinate);
        if (shortestPathsDirectory.containsKey(targetCoordinate)) {
            Optional<Integer> maybeShortestPath = shortestPathsDirectory.get(targetCoordinate).findShortestPathByRun(run);
            if (maybeShortestPath.isPresent()) {
                return maybeShortestPath;
            }
        }
        int thisCoordinateWeight = grid[targetCoordinate.row()][targetCoordinate.column()];
        Set<Direction> possibleDirections = new HashSet<>(Set.of(Direction.values()));
        if (run.direction != null) {
            possibleDirections.remove(run.direction.opposite());
        }
        if (run.direction != null && run.length >= 3) {
            possibleDirections.remove(run.direction);
        }
        Optional<Integer> shortestPathLength = Optional.empty();
        for (Direction possibleDirection : possibleDirections) {
            GridCoordinate previousStep = targetCoordinate.advance(possibleDirection.opposite());
            if (!previousStep.validOnGridOfSize(grid.length, grid[0].length)) {
                continue;
            }
            Run newRun = new Run(possibleDirection, possibleDirection == run.direction() ? run.length + 1 : 1);
            Optional<Integer> newValue = findShortestPathFromStart(grid, previousStep, shortestPathsDirectory, newRun, visited);
            if (newValue.isPresent()) {
                if (shortestPathLength.isEmpty()) {
                    shortestPathLength = newValue;
                } else {
                    shortestPathLength = shortestPathLength.map(soFar -> Integer.min(soFar, newValue.get()));
                }
            }
        }
        if (shortestPathLength.isEmpty()) {
            return Optional.empty();
        }
        if (!shortestPathsDirectory.containsKey(targetCoordinate)) {
            shortestPathsDirectory.put(targetCoordinate, new SpaceInfo());
        }
        shortestPathsDirectory.get(targetCoordinate).shortestPathPerRun.put(run, shortestPathLength.get() + thisCoordinateWeight);
        return Optional.of(shortestPathLength.get() + thisCoordinateWeight);
    }

    private static record Run(Direction direction, int length) {
    }

    private static class SpaceInfo {
        // Returns the shortest path to reach this space
        // that can admit each type of run
        Map<Run, Integer> shortestPathPerRun = new HashMap<>();

        Optional<Integer> findShortestPathByRun(Run run) {
            if (shortestPathPerRun.isEmpty()) {
                return Optional.empty();
            }
            if (run.direction == null) {
                return shortestPathPerRun.values().stream().min(Integer::compareTo);
            } else if (run.length == 3) {
                return shortestPathPerRun.entrySet().stream()
                    .filter(entry -> entry.getKey().direction != run.direction)
                    .map(Map.Entry::getValue)
                    .min(Integer::compareTo);
            } else {
                int maxRunInDirection = 3 - run.length;
                return shortestPathPerRun.entrySet().stream()
                    .filter(entry -> entry.getKey().direction != run.direction || entry.getKey().length <= maxRunInDirection)
                    .map(Map.Entry::getValue)
                    .min(Integer::compareTo);
            }
        }
    }

    public static enum Direction {
        UP, LEFT, DOWN, RIGHT;

        public Direction opposite() {
            Direction opposite = switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
            return opposite; // not sure why I can't return directly
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

        boolean validOnGridOfSize(int maxRow, int maxColumn) {
            return row >= 0 && column >= 0 && row < maxRow && column < maxColumn;
        }
    }

}
