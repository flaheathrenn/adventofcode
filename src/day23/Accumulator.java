package day23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import day23.GridUtils.Direction;
import day23.GridUtils.GridCoordinate;

public class Accumulator {
    // State
    List<String[]> rows = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        rows.add(parsedLine.row);
        return this;
    }

    // Extract solution
    public String star2() {
        // Create grid
        String[][] grid = rows.toArray(new String[rows.size()][rows.get(0).length]);

        // Find entry point
        GridCoordinate entryPoint = new GridCoordinate(0, String.join("", grid[0]).indexOf("."));
        GridCoordinate endPoint = new GridCoordinate(grid.length - 1, String.join("", grid[grid.length - 1]).indexOf("."));

        Set<Path> allPaths = new HashSet<>();

        findPaths(grid, entryPoint, endPoint, Direction.DOWN, allPaths);

        // System.out.println(allPaths);

        // for (int row = 0; row < grid.length; row++) {
        //     for (int column = 0; column < grid[0].length; column++) {
        //         final GridCoordinate here = new GridCoordinate(row, column);
        //         allPaths.stream().filter(path -> path.endPoints.contains(here)).findFirst()
        //             .ifPresentOrElse(p -> System.out.print("*"),
        //             () -> {
        //                 long onPaths = allPaths.stream().filter(path -> path.interiorPoints().contains(here)).count();
        //                 if (onPaths == 0) {
        //                     System.out.print(here.safeGet(grid).orElse("%"));
        //                 } else {
        //                     System.out.print(onPaths);
        //                 }
        //             });
        //     }
        //     System.out.println();
        // }

        return String.valueOf(longestPathLengthToEndpointFrom(entryPoint, endPoint, new HashSet<>(), allPaths));
        //return String.valueOf(longestPathLengthToEndpointFrom(entryPoint, endPoint, new ArrayList<>(), allPaths));
    }

    private int longestPathLengthToEndpointFrom(GridCoordinate entryPoint, GridCoordinate endPoint, Set<GridCoordinate> pointsSoFar, Set<Path> allPaths) {
        if (entryPoint.equals(endPoint)) {
            // base case, end recursion
            return pointsSoFar.size();
        }
        if (pointsSoFar.contains(entryPoint)) {
            // base case, end recursion
            return Integer.MIN_VALUE;
        }
        pointsSoFar.add(entryPoint);
        Set<Path> pathOptions = new HashSet<>();
        allPaths.stream()
            .filter(p -> p.adjoins(entryPoint))
            .filter(p -> Collections.disjoint(p.interiorPoints(), pointsSoFar))
            .forEach(pathOptions::add);

        int currentMaxLength = Integer.MIN_VALUE;
        for (Path p : pathOptions) {
            GridCoordinate otherEnd = p.endPoints.stream().filter(gc -> !gc.equals(entryPoint)).findFirst().orElseThrow(IllegalStateException::new);
            if (pointsSoFar.contains(otherEnd)) {
                continue;
            }
            Set<GridCoordinate> newPointsSoFar = new HashSet<>();
            newPointsSoFar.addAll(pointsSoFar);
            newPointsSoFar.addAll(p.interiorPoints());
            int thisLength = longestPathLengthToEndpointFrom(otherEnd, endPoint, newPointsSoFar, allPaths);
            currentMaxLength = Integer.max(currentMaxLength, thisLength);
        }
        // if (currentMaxLength >= 6422) {
        //     System.out.println(currentMaxLength);
        // }
        return currentMaxLength;
    }

    private void findPaths(String[][] grid, GridCoordinate entryPoint, GridCoordinate endPoint, Direction startDirection, Set<Path> allPaths) {
        Direction direction = startDirection;
        GridCoordinate currentPoint = entryPoint;
        Set<GridCoordinate> interiorPoints = new HashSet<>();

        while (true) {
            final Direction currentDirection = direction;
            GridCoordinate nextStep = currentPoint.advance(currentDirection);
            Set<Direction> possibleNextDirections = Arrays.stream(Direction.values())
                    .filter(d -> currentDirection.opposite() != d)
                    .filter(d -> nextStep.safeGet(grid).map(d::isValidStep).orElse(false))
                    .filter(d -> nextStep.advance(d).safeGet(grid).map(c -> !"#".equals(c)).orElse(false))
                    .collect(Collectors.toSet());
            // System.out.println("Next step: " + nextStep);
            if (endPoint.equals(nextStep)) {
                // System.out.println("Endpoint " + endPoint + " reached");
                // we've reached the end
                Path thisPath = new Path(Set.of(entryPoint, nextStep), interiorPoints);
                allPaths.add(thisPath);
                return;
            } else if (possibleNextDirections.isEmpty()) {
                // System.out.println("Dead end reached");
                // we've hit a dead end somehow, let's just forget this ever happened
                return;
            } else if (possibleNextDirections.size() == 1) {
                // we're still traversing a path normally, so just continue
                currentPoint = nextStep;
                interiorPoints.add(nextStep);
                direction = possibleNextDirections.iterator().next();
                // System.out.println("Must proceed in direction " + direction);
            } else {
                // we've hit a junction
                // System.out.println("Junction found at " + nextStep);
                Path thisPath = new Path(Set.of(entryPoint, nextStep), interiorPoints);
                if (!allPaths.add(thisPath)) {
                    // duplicate path found
                    return;
                }
                for (Direction possibleNextDirection : possibleNextDirections) {
                    // System.out.println("Testing path in direction " + possibleNextDirection + " from " + nextStep);
                    findPaths(grid, nextStep, endPoint, possibleNextDirection, allPaths);
                }
                return;
            }
        }

    }

    record Path(Set<GridCoordinate> endPoints, Set<GridCoordinate> interiorPoints) {
        boolean adjoins(GridCoordinate point) {
            return endPoints.contains(point);
        }
    }
}