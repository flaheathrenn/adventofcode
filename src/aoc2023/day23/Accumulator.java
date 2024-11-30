package aoc2023.day23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import aoc2023.day23.GridUtils.Direction;
import aoc2023.day23.GridUtils.GridCoordinate;

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

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[0].length; column++) {
                final GridCoordinate here = new GridCoordinate(row, column);
                allPaths.stream().filter(path -> path.endPoints.contains(here)).findFirst()
                    .ifPresentOrElse(p -> System.out.print("*"),
                    () -> {
                        // long onPaths = allPaths.stream().filter(path -> path.interiorPoints().contains(here)).count();
                        // if (onPaths == 0) {
                            System.out.print(here.safeGet(grid).orElse("%"));
                        // } else {
                        //     System.out.print(onPaths);
                        // }
                    });
            }
            System.out.println();
        }

        // convert to adjacency matrix
        Set<GridCoordinate> junctions = new TreeSet<>();
        for (Path path : allPaths) {
            junctions.addAll(path.endPoints());
        }

        int[][] adjacencyMatrix = new int[junctions.size()][junctions.size()];
        int matrixRow = 0;
        int matrixColumn = 0;
        for (GridCoordinate topJunction : junctions) {
            for (GridCoordinate sideJunction : junctions) {
                if (topJunction.equals(sideJunction)) {
                    adjacencyMatrix[matrixRow][matrixColumn] = Integer.MIN_VALUE;
                } else {
                    final int thisRow = matrixRow;
                    final int thisColumn = matrixColumn;
                    allPaths.stream().filter(p -> p.endPoints().equals(Set.of(topJunction, sideJunction)))
                        .findFirst().ifPresentOrElse(path -> {
                            adjacencyMatrix[thisRow][thisColumn] = path.interiorPoints.size();
                        }, () -> {
                            adjacencyMatrix[thisRow][thisColumn] = Integer.MIN_VALUE;
                        });
                }
                matrixRow++;
            }
            matrixColumn++;
            matrixRow = 0;
        }

        System.out.println();
        System.out.print("\t");
        for (GridCoordinate junction : junctions) {
            System.out.print(junction);
            System.out.print("\t");
        }
        System.out.println();
        int i = 0;
        for (GridCoordinate junction : junctions) {
            System.out.print(junction);
            System.out.print("\t");
            for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                if (adjacencyMatrix[i][j] == Integer.MIN_VALUE) {
                    System.out.print("X");
                } else {
                    System.out.print(adjacencyMatrix[i][j]);
                }
                System.out.print("\t");
            }
            i++;
            System.out.println();
        }

        long roughNumberOfPaths = 1;
        for (int[] matrixRowArray : adjacencyMatrix) {
            long items = Arrays.stream(matrixRowArray).filter(x -> x != Integer.MIN_VALUE).count();
            if (items == 1L) {
                continue;
            }
            roughNumberOfPaths *= items - 1;
        }
        System.out.println("There seem to be roughly " + roughNumberOfPaths + " paths");

        GridCoordinate realEndPoint = allPaths.stream().filter(p -> p.endPoints().contains(endPoint))
            .map(Path::endPoints)
            .flatMap(Set::stream)
            .filter(gc -> !gc.equals(endPoint))
            .findFirst().orElseThrow(IllegalStateException::new);

        int lastDistance = 1 + allPaths.stream().filter(p -> p.endPoints().contains(endPoint)).map(Path::interiorPoints).map(Set::size).findFirst().orElseThrow(IllegalStateException::new);

        return String.valueOf(lastDistance + longestPathLengthToEndpointFrom(entryPoint, realEndPoint, new HashSet<>(), allPaths));
    }

    private int longestPathLengthToEndpointFrom(GridCoordinate entryPoint, GridCoordinate endPoint, Set<GridCoordinate> pointsSoFar, Set<Path> allPaths) {
        if (entryPoint.equals(endPoint)) {
            // base case, end recursion
            return 0;
        }
        if (pointsSoFar.contains(entryPoint)) {
            // base case, end recursion
            return Integer.MIN_VALUE;
        }
        pointsSoFar.add(entryPoint);
        Set<Path> pathOptions = new HashSet<>();
        allPaths.stream()
            .filter(p -> p.adjoins(entryPoint))
            //.filter(p -> Collections.disjoint(p.endPoints(), pointsSoFar))
            .forEach(pathOptions::add);

        int currentMaxLength = Integer.MIN_VALUE;
        for (Path p : pathOptions) {
            GridCoordinate otherEnd = p.endPoints.stream().filter(gc -> !gc.equals(entryPoint)).findFirst().orElseThrow(IllegalStateException::new);
            if (pointsSoFar.contains(otherEnd)) {
                continue;
            }
            int thisLength = p.interiorPoints.size() + 1 + longestPathLengthToEndpointFrom(otherEnd, endPoint, new HashSet<>(pointsSoFar), allPaths);
            currentMaxLength = Integer.max(currentMaxLength, thisLength);
        }
        System.out.println(currentMaxLength);
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