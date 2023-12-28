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
    public String star1() {
        // Create grid
        String[][] grid = rows.toArray(new String[rows.size()][rows.get(0).length]);

        // Find entry point
        GridCoordinate entryPoint = new GridCoordinate(0, String.join("", grid[0]).indexOf("."));
        GridCoordinate endPoint = new GridCoordinate(grid.length - 1, String.join("", grid[grid.length - 1]).indexOf("."));

        Set<Path> allPaths = new HashSet<>();
        Set<Junction> allJunctions = new HashSet<>();

        Set<Path> pathsFromEntryPoint = new HashSet<>();
        Junction entryPointAsJunction = new Junction(entryPoint, pathsFromEntryPoint);
        allJunctions.add(entryPointAsJunction);

        findPaths(grid, entryPoint, endPoint, entryPointAsJunction, Direction.DOWN, allPaths, allJunctions);

        System.out.println(allPaths);
        System.out.println(allJunctions);

        return String.valueOf(longestPathLengthToEndpointFrom(entryPoint, endPoint, new ArrayList<>(), allPaths, allJunctions));
    }

    private int longestPathLengthToEndpointFrom(GridCoordinate entryPoint, GridCoordinate endPoint, List<Path> pathSoFar, Set<Path> allPaths, Set<Junction> allJunctions) {
        if (endPoint.equals(entryPoint)) {
            System.out.println("Reached endpoint, terminating");
            return 0;
        }
        Junction startJunction = allJunctions.stream().filter(j -> j.location().equals(entryPoint)).findFirst().orElseThrow(IllegalArgumentException::new);
        Set<Path> exitPaths = new HashSet<>(startJunction.exitPaths());
        List<GridCoordinate> pointsSoFar = pathSoFar.stream().flatMap(p -> p.interiorPoints().stream()).toList();
        List<Path> actualExitPaths = exitPaths.stream().filter(exitPath -> Collections.disjoint(exitPath.interiorPoints(), pointsSoFar)).toList();
        if (actualExitPaths.isEmpty()) {
            System.out.println("No path exists from " + entryPoint + " to endpoint after " + pathSoFar + ", terminating");
            return Integer.MIN_VALUE;
        }

        return actualExitPaths.stream().map(exitPath -> {
            List<Path> newPathSoFar = new ArrayList<>();
            newPathSoFar.addAll(pathSoFar);
            newPathSoFar.add(exitPath);
            System.out.println("Testing path " + exitPath);
            return exitPath.length() + longestPathLengthToEndpointFrom(exitPath.endpoint(), endPoint, newPathSoFar, allPaths, allJunctions);
        }).reduce(Integer::max).orElse(0);
    }

    private void findPaths(String[][] grid, GridCoordinate entryPoint, GridCoordinate endPoint, Junction precedingJunction, Direction startDirection, Set<Path> allPaths, Set<Junction> allJunctions) {
        // System.out.println("Entered findPath");
        Direction direction = startDirection;
        GridCoordinate currentPoint = entryPoint;
        int pathLength = 0;
        Set<GridCoordinate> interiorPoints = new HashSet<>();

        while (true) {
            // System.out.println("Looping: pathLength is " + pathLength);
            pathLength++;
            final Direction currentDirection = direction;
            GridCoordinate nextStep = currentPoint.advance(currentDirection);
            interiorPoints.add(nextStep);
            Set<Direction> possibleNextDirections = Arrays.stream(Direction.values())
                    .filter(d -> currentDirection.opposite() != d)
                    .filter(d -> nextStep.safeGet(grid).map(d::isValidStep).orElse(false))
                    .filter(d -> nextStep.advance(d).safeGet(grid).map(c -> !"#".equals(c)).orElse(false))
                    .collect(Collectors.toSet());
            // System.out.println("Next step: " + nextStep);
            if (endPoint.equals(nextStep)) {
                // System.out.println("Endpoint " + endPoint + " reached");
                // we've reached the end
                Path thisPath = new Path(pathLength, startDirection, nextStep, interiorPoints);
                allPaths.add(thisPath);
                if (precedingJunction != null) {
                    precedingJunction.exitPaths.add(thisPath);
                }
                return;
            } else if (possibleNextDirections.isEmpty()) {
                // System.out.println("Dead end reached");
                // we've hit a dead end somehow, let's just forget this ever happened
                return;
            } else if (possibleNextDirections.size() == 1) {
                // we're still traversing a path normally, so just continue
                currentPoint = nextStep;
                direction = possibleNextDirections.iterator().next();
                // System.out.println("Must proceed in direction " + direction);
            } else {
                // we've hit a junction
                // System.out.println("Junction found at " + nextStep);
                Junction junctionAtEnd = new Junction(nextStep, new HashSet<Path>());
                allJunctions.add(junctionAtEnd);
                Path thisPath = new Path(pathLength, startDirection, nextStep, interiorPoints);
                if (!allPaths.add(thisPath)) {
                    // duplicate path found
                    return;
                }
                if (precedingJunction != null) {
                    precedingJunction.exitPaths().add(thisPath);
                }
                for (Direction possibleNextDirection : possibleNextDirections) {
                    // System.out.println("Testing path in direction " + possibleNextDirection + " from " + nextStep);
                    findPaths(grid, nextStep, endPoint, junctionAtEnd, possibleNextDirection, allPaths, allJunctions);
                }
                return;
            }
        }

    }

    record Path(int length, Direction startDirection, GridCoordinate endpoint, Set<GridCoordinate> interiorPoints) {
    }

    record Junction(GridCoordinate location, Set<Path> exitPaths) {
    }
}