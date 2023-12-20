package day18;

import day18.GridUtils.GridCoordinate;

public class Accumulator {
    // State
    String[][] grid;
    GridCoordinate currentPosition;

    // int minRow;
    // int minCol;
    // int maxRow;
    // int maxCol;

    public Accumulator() {
        this.grid = new String[299][341];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = ".";
            }
        }
        this.currentPosition = new GridCoordinate(148, 4);
        currentPosition.writeToGrid(grid, "#");
        // this.minRow = 1000;
        // this.maxRow = 0;
        // this.minCol = 1000;
        // this.maxCol = 0;
    }

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        for (int i = 0; i < parsedLine.distance; i++) {
            currentPosition = currentPosition.advance(parsedLine.direction);
            // minRow = Integer.min(currentPosition.row(), minRow);
            // maxRow = Integer.max(currentPosition.row(), maxRow);
            // minCol = Integer.min(currentPosition.column(), minCol);
            // maxCol = Integer.max(currentPosition.column(), maxCol);
            if (!currentPosition.validOnGrid(grid)) {
                System.err.println("Reached illegal grid position " + currentPosition + ", change parameters");
                throw new IllegalStateException();
            }
            currentPosition.writeToGrid(grid, "#");
        }
        return this;
    }

    // Extract solution
    public String star1() {
        // debug print
        GridUtils.printGrid(grid);

        // System.out.println("minRow:" + minRow);
        // System.out.println("maxRow:" + maxRow);
        // System.out.println("minCol:" + minCol);
        // System.out.println("maxCol:" + maxCol);

        int capacity = 0;
        for (int row = 0; row < grid.length; row++) {
            boolean inside = false;
            boolean onLoop = false;
            boolean spaceAbove = false;
            boolean spaceBelow = true;
            for (int column = 0; column < grid[row].length; column++) {
                if ("#".equals(grid[row][column])) {
                    capacity++;
                    if (onLoop) {
                        continue;
                    }
                    onLoop = true;
                    spaceAbove = !"#".equals(grid[row - 1][column]);
                    spaceBelow = !"#".equals(grid[row + 1][column]);
                } else {
                    if (onLoop) { // transitioning b/w empty space and trench
                        if (spaceAbove && spaceBelow) {
                            inside = !inside; // must have been a crossing
                        }
                        boolean newSpaceAbove = !"#".equals(grid[row - 1][column - 1]);
                        boolean newSpaceBelow = !"#".equals(grid[row + 1][column - 1]);
                        if ((spaceAbove && newSpaceAbove) || (spaceBelow && newSpaceBelow)) {
                            // U-shape so haven't crossed over
                        } else {
                            // staggered line so have crossed over
                            inside = !inside;
                        }
                    }
                    onLoop = false;
                    if (inside) {
                        grid[row][column] = "I";
                        capacity++;
                    }
                }
            }
        }

        System.out.println();
        System.out.println();
        GridUtils.printGrid(grid);
        System.out.println();

        return String.valueOf(capacity);
    }
}
