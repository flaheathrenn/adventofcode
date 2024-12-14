package aoc2024.day14;

public class Accumulator {
    // State
    long spaceWidth = 101;
    long spaceHeight = 103;
    long seconds = 100;

    // int[][] grid = new int[(int) spaceHeight][(int) spaceWidth];

    int trCount, tlCount, brCount, blCount = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        // System.out.println(String.format("Initial position (%d,%d), initial velocity (%d,%d)", parsedLine.px, parsedLine.py, parsedLine.vx, parsedLine.vy));
        long finalX = modNonNegative(parsedLine.px + seconds * parsedLine.vx, spaceWidth);
        long finalY = modNonNegative(parsedLine.py + seconds * parsedLine.vy, spaceHeight);
        // grid[(int) finalY][(int) finalX]++;
        // System.out.println(String.format("Final coordinate calculated as (%d,%d)", finalX, finalY));
        boolean l = finalX < ((spaceWidth - 1) / 2);
        boolean r = finalX > ((spaceWidth - 1) / 2);
        boolean t = finalY < ((spaceHeight - 1) / 2);
        boolean b = finalY > ((spaceHeight - 1) / 2);
        if (t && l) {
            tlCount++;
        }
        if (t && r) {
            trCount++;
        }
        if (b && l) {
            blCount++;
        }
        if (b && r) {
            brCount++;
        }
        return this;
    }

    // Extract solution
    public String star1() {
        // for (int i = 0; i < grid.length; i++) {
        //     for (int j = 0; j < grid[i].length; j++) {
        //         if (grid[i][j] == 0) {
        //             System.out.print(".");
        //         } else {
        //             System.out.print(grid[i][j]);
        //         }
        //     }
        //     System.out.println();
        // }
        return Long.toString(blCount * brCount * tlCount * trCount);
    }

    private long modNonNegative(long a, long b) {
        return (a % b + b) % b;
    }
}
