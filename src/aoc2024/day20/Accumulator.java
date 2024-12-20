package aoc2024.day20;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import global.GridUtils;
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

        GridCoordinate start = GridUtils.find(grid, 'S');
        Map<GridCoordinate, Integer> distanceMap = new HashMap<>();
        Set<Set<GridCoordinate>> cheats = new HashSet<>();

        GridCoordinate current = start;
        Direction currentDirection = null;
        int currentDistance = 0;
        distanceMap.put(current, currentDistance);
        boolean terminate = false;
        while (!terminate) {
            currentDistance++;
            Direction nextDirection = null;
            for (Direction d: Direction.values()) {
                if (d.flip() == currentDirection) {
                    continue; // no doubling back
                }
                GridCoordinate step = current.step(d);
                switch (grid[step.i()][step.j()]) {
                    case 'E':
                        terminate = true;
                        // fall through to '.' case
                    case '.':
                        // correct next step along path
                        nextDirection = d;
                        distanceMap.put(step, currentDistance);
                        break;
                    case '#':
                        // potential cheat
                        GridCoordinate anotherStep = step.step(d);
                        if (!anotherStep.isWithin(grid)) {
                            continue;
                        }
                        if (grid[anotherStep.i()][anotherStep.j()] == '#') {
                            continue;
                        }
                        cheats.add(Set.of(current, anotherStep));
                        break;
                }
            }
            currentDirection = nextDirection;
            current = current.step(nextDirection);
        }

        Map<Integer, Integer> cheatsDirectory = new HashMap<>();
        for (Set<GridCoordinate> cheat : cheats) {
            Iterator<GridCoordinate> iter = cheat.iterator();
            int distance = Math.abs(distanceMap.get(iter.next()) - distanceMap.get(iter.next())) - 2;
            cheatsDirectory.put(distance, cheatsDirectory.containsKey(distance) ? cheatsDirectory.get(distance) + 1 : 1);
        }

        int solution = 0;
        for (Map.Entry<Integer, Integer> cheatSummary : cheatsDirectory.entrySet()) {
            if (cheatSummary.getKey() >= 100) { // these cheats save at least 100ps
                solution += cheatSummary.getValue();
            }
        }

        return Integer.toString(solution);
    }

    public String star2() {
        char[][] grid = rows.toArray(new char[rows.size()][]);

        GridCoordinate start = GridUtils.find(grid, 'S');
        Map<Integer, GridCoordinate> distanceMapReversed = new HashMap<>();

        GridCoordinate current = start;
        Direction currentDirection = null;
        int currentDistance = 0;
        distanceMapReversed.put(currentDistance, current);
        boolean terminate = false;
        while (!terminate) {
            currentDistance++;
            Direction nextDirection = null;
            testDirections: for (Direction d: Direction.values()) {
                if (d.flip() == currentDirection) {
                    continue; // no doubling back
                }
                GridCoordinate step = current.step(d);
                switch (grid[step.i()][step.j()]) {
                    case 'E':
                        terminate = true;
                        // fall through to '.' case
                    case '.':
                        // correct next step along path
                        nextDirection = d;
                        distanceMapReversed.put(currentDistance, step);
                        break testDirections;
                    default:
                        break;
                }
            }
            currentDirection = nextDirection;
            current = current.step(nextDirection);
        }

        int maxDistance = distanceMapReversed.keySet().stream().max(Integer::compare).orElse(0);
        int minSaving = 100;
        int maxCheatDistance = 20;
        int solution = 0;

        for (int i = 0; i <= (maxDistance - minSaving); i++) {
            for (int j = i + minSaving; j <= maxDistance; j++) {
                GridCoordinate cheatStart = distanceMapReversed.get(i);
                GridCoordinate cheatEnd = distanceMapReversed.get(j);
                if (cheatStart.manhattanDistance(cheatEnd) <= maxCheatDistance
                    && (j - i) - cheatStart.manhattanDistance(cheatEnd) >= minSaving) {
                    solution++;
                }
            }
        }

        return Integer.toString(solution);
    }
}
