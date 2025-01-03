package aoc2024.day15;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import global.GridUtils.Direction;
import global.GridUtils.GridCoordinate;

public class Accumulator {
    // State
    boolean mapMode = true;
    List<char[]> rows = new ArrayList<>();
    List<char[]> instructions = new ArrayList<>();

    Set<GridCoordinate> walls = new HashSet<>();
    List<Box> boxes = new ArrayList<>();
    Box robot; // treat robot as a box for now

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (parsedLine.row.length == 0) {
            mapMode = false;
        } else if (mapMode) {
            rows.add(parsedLine.row);
        } else {
            instructions.add(parsedLine.row);
        }
        return this;
    }

    // Extract solution
    public String star1() {
        initVariables();
        // printDebugGrid(boxes, walls, robot);
        // System.out.println("==============================");
        for (char[] instructionRow : instructions) {
            for (char instruction : instructionRow) {
                Direction d = Direction.fromChar(instruction);
                robot.move(d, boxes, walls);
                // printDebugGrid(boxes, walls, robot);
                // System.out.println("==============================");
            }
        }
        return Integer.toString(boxes.stream().map(Box::getGPSCoordinate).reduce(0, Integer::sum));
    }

    private void initVariables() {
        char[][] grid = rows.toArray(new char[rows.size()][]);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                switch (grid[i][j]) {
                    case '#': {
                        walls.add(new GridCoordinate(i, j));
                        break;
                    }
                    case 'O': {
                        boxes.add(new Box(new GridCoordinate(i, j)));
                        break;
                    }
                    case '@': {
                        robot = new Box(new GridCoordinate(i, j));
                        break;
                    }
                    default: {
                        // do nothing
                    }
                }
            }
        }
    }

    public static void printDebugGrid(List<Box> boxes, Set<GridCoordinate> walls, Box robot) {
        int maxI = 0;
        maxI = Integer.max(maxI,
                boxes.stream().map(b -> b.gridCoordinate).map(GridCoordinate::i).max(Integer::compare).orElse(0));
        maxI = Integer.max(maxI, walls.stream().map(GridCoordinate::i).max(Integer::compare).orElse(0));
        maxI = Integer.max(maxI, robot.gridCoordinate.i());
        int maxJ = 0;
        maxJ = Integer.max(maxJ,
                boxes.stream().map(b -> b.gridCoordinate).map(GridCoordinate::j).max(Integer::compare).orElse(0));
        maxJ = Integer.max(maxJ, walls.stream().map(GridCoordinate::j).max(Integer::compare).orElse(0));
        maxJ = Integer.max(maxJ, robot.gridCoordinate.j());
        for (int i = 0; i <= maxI; i++) {
            for (int j = 0; j <= maxJ; j++) {
                GridCoordinate me = new GridCoordinate(i, j);
                if (robot.gridCoordinate.equals(me)) {
                    System.out.print("@");
                } else if (walls.contains(me)) {
                    System.out.print("#");
                } else if (boxes.contains(new Box(me))) {
                    System.out.print("O");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
}
