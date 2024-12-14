package aoc2024.day14;

public class Accumulator {
    // State
    long spaceWidth = 101;
    long spaceHeight = 103;
    long seconds = 10000;

    int[][][] grid = new int[(int) spaceHeight][(int) spaceWidth][(int) seconds];

    int trCount, tlCount, brCount, blCount = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        for (int s = 0; s < seconds; s++) {
            long finalX = modNonNegative(parsedLine.px + s * parsedLine.vx, spaceWidth);
            long finalY = modNonNegative(parsedLine.py + s * parsedLine.vy, spaceHeight);
            grid[(int) finalY][(int) finalX][s]++;
        }
        long finalX = modNonNegative(parsedLine.px + 100 * parsedLine.vx, spaceWidth);
        long finalY = modNonNegative(parsedLine.py + 100 * parsedLine.vy, spaceHeight);
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
        return Long.toString(blCount * brCount * tlCount * trCount);
    }

    // Extract solution
    public String star2() {
        for (int s = 0; s < seconds; s++) {
            int longestHorizontalLine = 0;
            int currentHorizontalLine;
            for (int i = 0; i < grid.length; i++) {
                currentHorizontalLine = 0;
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j][s] != 0) {
                        currentHorizontalLine++;
                    } else {
                        if (currentHorizontalLine > longestHorizontalLine) {
                            longestHorizontalLine = currentHorizontalLine;
                        }
                        currentHorizontalLine = 0;
                    }
                }
            }
            if (longestHorizontalLine > 5) {
                System.out.println("At " + s + " seconds, longest horizontal line was " + longestHorizontalLine);
                for (int i = 0; i < grid.length; i++) {
                    currentHorizontalLine = 0;
                    for (int j = 0; j < grid[i].length; j++) {
                        if (grid[i][j][s] == 0) {
                            System.out.print(".");
                        } else {
                            System.out.print(grid[i][j][s]);
                        }
                    }
                    System.out.println();
                }
                System.out.println();
            }
        }
        return "";
    }

    private long modNonNegative(long a, long b) {
        return (a % b + b) % b;
    }
}
