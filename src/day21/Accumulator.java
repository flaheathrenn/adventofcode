package day21;

import java.util.ArrayList;
import java.util.List;

public class Accumulator {
    private static final int STEP_COUNT = 50;

    // State
    List<String[]> gridRows = new ArrayList<>();
    int sX = -1;
    int sY = -1;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        gridRows.add(parsedLine.row);
        if (parsedLine.sRank != -1) {
            sX = parsedLine.sRank;
            sY = gridRows.size() - 1;
        }
        return this;
    }

    // Extract solution
    public String star1() {
        String[][] grid = gridRows.toArray(new String[gridRows.size()][gridRows.get(0).length]);

        // print for debugging
        // for (int rowIndex = 0; rowIndex < grid.length; rowIndex++) {
        //     for (int columnIndex = 0; columnIndex < grid[rowIndex].length; columnIndex++) {
        //         System.out.print(grid[rowIndex][columnIndex]);
        //     }
        //     System.out.println();
        // }
        // System.out.println("-----------");

        for (int steps = 0; steps < STEP_COUNT; steps++) {
            grid = step(grid);
            // for (int rowIndex = 0; rowIndex < grid.length; rowIndex++) {
            //     for (int columnIndex = 0; columnIndex < grid[rowIndex].length; columnIndex++) {
            //         System.out.print(grid[rowIndex][columnIndex]);
            //     }
            //     System.out.println();
            // }
            // System.out.println("-----------");
        }

        return "";
    }

    private String[][] step(String[][] grid) {
        String[][] resultGrid = new String[grid.length][grid[0].length];
        int ohCount = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[0].length; column++) {
                if (grid[row][column].equals("#")) {
                    resultGrid[row][column] = "#";
                    continue;
                }
                String surroundings = safeSurroundings(grid, row, column);
                if (surroundings.contains("O") || surroundings.contains("S")) {
                    resultGrid[row][column] = "O";
                    ohCount++;
                    continue;
                }
                resultGrid[row][column] = ".";
            }
        }
        System.out.println(ohCount);
        return resultGrid;
    }

    private String safeSurroundings(String[][] grid, int row, int column) {
        String up = safeGet(grid, row - 1, column);
        String left = safeGet(grid, row, column - 1);
        String down = safeGet(grid, row + 1, column);
        String right = safeGet(grid, row, column + 1);
        return up + left + down + right;
    }

    private String safeGet(String[][] grid, int row, int column) {
        // TODO: this approach doesn't work because the map is infinite, not toroidal
        return grid[Math.floorMod(row, grid.length)][Math.floorMod(column, grid[0].length)];
    }
}
