package day10;

import java.util.Set;

import day10.Tracker.Direction;

public class Accumulator {
    // Hardcode some properties of the input from manual inspection
    private static final int GRID_SIZE = 200; // size of input grid
    private static final String S_VALUE = "L"; // the actual letter that the S covers
    private static final Direction INITIAL_DIRECTION = Direction.EAST; // direction to proceed from S

    // State
    String[][] grid = new String[GRID_SIZE][GRID_SIZE]; // row, then column
    int currentLine = 0;
    int sIndexRow;
    int sIndexColumn;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        grid[currentLine] = parsedLine.items;
        if (parsedLine.sIndex != -1) {
            sIndexRow = currentLine;
            sIndexColumn = parsedLine.sIndex;
        }
        currentLine++;
        return this;
    }

    // Extract solution
    public String star1() {
        System.out.println("Starting at " + sIndexRow + ", " + sIndexColumn);
        Tracker tracker = new Tracker();
        tracker.row = sIndexRow;
        tracker.column = sIndexColumn;
        tracker.direction = INITIAL_DIRECTION; // hardcoded start direction from manual inspection of input
        String currentTile = "S";
        int steps = 0;
        do {
            System.out.println("Advancing from tile " + currentTile + " in direction " + tracker.direction);
            tracker.advance(currentTile, tracker.direction);
            currentTile = grid[tracker.row][tracker.column];
            steps++;
        } while (!currentTile.equals("S"));
        System.out.println("Returned to start tile in " + steps + " steps");
        return String.valueOf(steps / 2);
    }

    public String star2() {
        // Create a copy of the path's route in memory
        boolean pathHolder[][] = new boolean[GRID_SIZE][GRID_SIZE];
        Tracker tracker = new Tracker();
        tracker.row = sIndexRow;
        tracker.column = sIndexColumn;
        tracker.direction = INITIAL_DIRECTION;
        String currentTile = "S";
        do {
            tracker.advance(currentTile, tracker.direction);
            pathHolder[tracker.row][tracker.column] = true;
            currentTile = grid[tracker.row][tracker.column];
        } while (!currentTile.equals("S"));

        // replace S with its actual corner value - hardcoding
        grid[sIndexRow][sIndexColumn] = S_VALUE;

        // the basic principle is to step through the area, keeping track of the number of times
        // we've crossed the loop
        // if it's odd, we're inside the loop, if it's even, we're outside the loop
        int area = 0;
        boolean insideLoop = false;
        String tileMemory = "";
        for (int r = 0; r < pathHolder.length; r++) {
            for (int c = 0; c < pathHolder[0].length; c++) {
                if (pathHolder[r][c]) { // if this tile is on the path...
                    insideLoop = switch (grid[r][c]) {
                        case "|" -> !insideLoop; // we've crossed the loop
                        case "F", "7", "L", "J" -> { // corners are a bit more complicated
                            if (tileMemory.isEmpty()) {
                                tileMemory = grid[r][c];
                                yield insideLoop;
                            } else {
                                if (staggeredLine(tileMemory, grid[r][c])) {
                                    // we've just encountered a wiggle, which is basically just a wider |
                                    // so we've crossed the loop
                                    tileMemory = "";
                                    yield !insideLoop;
                                } else {
                                    // we've encountered a full U-shape, which doesn't change our loop status
                                    tileMemory = "";
                                    yield insideLoop;
                                }
                            }
                        }
                        default -> insideLoop; // don't change
                    };
                    System.out.print("#"); // path
                } else {
                    if (insideLoop) { // if this is a blank tile inside the loop...
                        area++; // ...add it to the area
                        System.out.print("I");
                    } else {
                        System.out.print(" ");
                    }
                }
            }
            insideLoop = false; // pretty sure this shouldn't be necessary but it's done now
            System.out.println();

        }

        return String.valueOf(area);
    }

    /**
     * Determine if the two corners of a line represent a wiggle or a full turn
     */
    private boolean staggeredLine(String tileMemory, String currentTile) {
        Set<String> inputs = Set.of(tileMemory, currentTile);
        return inputs.equals(Set.of("F", "J"))
            || inputs.equals(Set.of("L", "7"));
    }
}
