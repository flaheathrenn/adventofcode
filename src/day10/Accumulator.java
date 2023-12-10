package day10;

import day10.Tracker.Direction;

public class Accumulator {
    // State
    String[][] grid = new String[200][200]; // row, then column
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
        tracker.direction = Direction.EAST; // hardcoded start direction from manual inspection of input
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
}
