package aoc2024.day10;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import global.GridUtils.Direction;
import global.GridUtils.GridCoordinate;

public class Accumulator {
    // State
    List<char[]> rows = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        rows.add(parsedLine.row);
        return this;
    }

    // Extract solution
    public String star1() {
        char[][] grid = rows.toArray(new char[rows.size()][]);

        Map<Character, Set<GridCoordinate>> locations = new HashMap<>();
        List.of('0','1','2','3','4','5','6','7','8','9')
            .forEach(d -> locations.put(d, new HashSet<>()));
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                locations.get(grid[i][j]).add(new GridCoordinate(i, j));
            }
        }

        Map<GridCoordinate, Set<GridCoordinate>> reachableNinesMap = new HashMap<>();
        for (GridCoordinate nine : locations.get('9')) {
            reachableNinesMap.put(nine, Set.of(nine));
        }

        for (Character d : List.of('8', '7', '6', '5', '4', '3', '2', '1', '0')) {
            Map<GridCoordinate, Set<GridCoordinate>> updatedReachableNinesMap = new HashMap<>();
            for (GridCoordinate digitLocation : locations.get(d)) {
                Set<GridCoordinate> reachableNines = new HashSet<>();
                for (Direction dir : Direction.values()) {
                    if (reachableNinesMap.containsKey(digitLocation.step(dir))) {
                        reachableNines.addAll(reachableNinesMap.get(digitLocation.step(dir)));
                    }
                }
                updatedReachableNinesMap.put(digitLocation, reachableNines);
            }
            reachableNinesMap = updatedReachableNinesMap;
        }

        int star1solution = 0;
        for (GridCoordinate zeroLocation : locations.get('0')) {
            star1solution += reachableNinesMap.get(zeroLocation).size();
        }

        return String.valueOf(star1solution);
    }

    // Extract solution
    public String star2() {
        char[][] grid = rows.toArray(new char[rows.size()][]);

        Map<Character, Set<GridCoordinate>> locations = new HashMap<>();
        List.of('0','1','2','3','4','5','6','7','8','9')
            .forEach(d -> locations.put(d, new HashSet<>()));
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                locations.get(grid[i][j]).add(new GridCoordinate(i, j));
            }
        }

        Integer[][] scoresGrid = new Integer[grid.length][grid[0].length];
        for (GridCoordinate nine : locations.get('9')) {
            nine.write(scoresGrid, 1);
        }

        for (Character d : List.of('8', '7', '6', '5', '4', '3', '2', '1', '0')) {
            Integer[][] updatedScoresGrid = new Integer[scoresGrid.length][scoresGrid[0].length];
            for (GridCoordinate digitLocation : locations.get(d)) {
                int score = 0;
                for (Direction dir : Direction.values()) {
                    score += Optional.ofNullable(digitLocation.step(dir).read(scoresGrid)).orElse(0);
                }
                digitLocation.write(updatedScoresGrid, score);
            }
            scoresGrid = updatedScoresGrid;
        }

        int star2solution = 0;
        for (GridCoordinate zeroLocation : locations.get('0')) {
            star2solution += zeroLocation.read(scoresGrid);
        }

        return String.valueOf(star2solution);
    }
}
