package aoc2023.day21;

import java.util.ArrayList;
import java.util.List;

public class AccumulatorForStar1 {
    // State
    List<String[]> gridRows = new ArrayList<>();
    int sX = -1;
    int sY = -1;
    int ohCount = 0;

    // Update state from parsed line
    public AccumulatorForStar1 update(ParsedLine parsedLine) {
        gridRows.add(parsedLine.row);
        if (parsedLine.sRank != -1) {
            sX = parsedLine.sRank;
            sY = gridRows.size() - 1;
        }
        return this;
    }

    // Extract solution
    public String star1(long stepCount) {
        String[][] grid = gridRows.toArray(new String[gridRows.size()][gridRows.get(0).length]);

        // print for debugging
        // for (int rowIndex = 0; rowIndex < grid.length; rowIndex++) {
        //     for (int columnIndex = 0; columnIndex < grid[rowIndex].length; columnIndex++) {
        //         System.out.print(grid[rowIndex][columnIndex]);
        //     }
        //     System.out.println();
        // }
        // System.out.println("-----------");

        for (int enlargements = 0; enlargements < 2; enlargements++) {
            grid = enlargeGrid(grid);
        }

        for (int steps = 0; steps < stepCount; steps++) {
            grid = step(grid);
            // for (int rowIndex = 0; rowIndex < grid.length; rowIndex++) {
            //     for (int columnIndex = 0; columnIndex < grid[rowIndex].length; columnIndex++) {
            //         System.out.print(grid[rowIndex][columnIndex]);
            //     }
            //     System.out.println();
            // }
            // System.out.println("-----------");
        }

        return String.valueOf(ohCount);
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
        this.ohCount = ohCount;
        return resultGrid;
    }

    private String[][] enlargeGrid(String[][] inputGrid) {
        String[][] outputGrid = new String[inputGrid.length * 3][inputGrid[0].length * 3];
        for (int i = 0; i < outputGrid.length; i++) {
            for (int j = 0; j < outputGrid[0].length; j++) {
                outputGrid[i][j] = inputGrid[i % inputGrid.length][j % inputGrid[0].length];
                if (outputGrid[i][j].equals("S") && (i < inputGrid.length || i > 2 * inputGrid.length || j < inputGrid[0].length || j > 2 * inputGrid[0].length)) {
                    outputGrid[i][j] = ".";
                }
            }
        }
        return outputGrid;
    }

    private String safeSurroundings(String[][] grid, int row, int column) {
        String up = safeGet(grid, row - 1, column);
        String left = safeGet(grid, row, column - 1);
        String down = safeGet(grid, row + 1, column);
        String right = safeGet(grid, row, column + 1);
        return up + left + down + right;
    }

    private String safeGet(String[][] grid, int row, int column) {
        if (row < 0 || column < 0 || row >= grid.length || column >= grid.length) {
            return "";
        }
        return grid[Math.floorMod(row, grid.length)][Math.floorMod(column, grid[0].length)];
    }
}
