package day21;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Accumulator {

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
        Integer[][] resultGrid = manhattanDistance(grid);
        Integer[][] resultGridIfBlank = manhattanDistanceOnABlankGrid(grid);
        // print for debugging
        for (int rowIndex = 0; rowIndex < grid.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < grid[rowIndex].length; columnIndex++) {
                // System.out.print(printable(resultGrid[rowIndex][columnIndex]));
                if (grid[rowIndex][columnIndex].equals("S")) {
                    System.out.print("S");
                } else if (grid[rowIndex][columnIndex].equals("#")) {
                    System.out.print("#");
                } else if (!resultGridIfBlank[rowIndex][columnIndex].equals(resultGrid[rowIndex][columnIndex])) {
                    if (resultGrid[rowIndex][columnIndex] == null) {
                        System.out.print("X"); // unreachable space
                    } else {
                        System.out.print(resultGrid[rowIndex][columnIndex] - resultGridIfBlank[rowIndex][columnIndex]);
                    }
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        return "";
    }

    private String printable(Integer i) {
        if (i == null) {
            return ".";
        }
        if (i < 10) {
            return String.valueOf(i);
        }
        return switch (i) {
            case 10 -> "A";
            case 11 -> "B";
            case 12 -> "C";
            case 13 -> "D";
            case 14 -> "E";
            case 15 -> "F";
            case 16 -> "G";
            case 17 -> "H";
            case 18 -> "I";
            case 19 -> "J";
            case 20 -> "K";
            default -> "?";
        };
    }

    private Integer[][] manhattanDistance(String[][] grid) {
        Integer[][] resultGrid = new Integer[grid.length][grid[0].length];
        for (int times = 0; times < 2500; times++) {
            for (int row = 0; row < grid.length; row++) {
                for (int column = 0; column < grid[row].length; column++) {
                    if (grid[row][column].equals("#")) {
                        continue; // keep as null
                    }
                    if (grid[row][column].equals("S")) {
                        resultGrid[row][column] = 0;
                        continue;
                    }
                    List<Integer> surroundings = safeSurroundings(resultGrid, row, column);
                    Optional<Integer> minAdjacentNumber = surroundings.stream().min(Integer::compareTo);
                    if (minAdjacentNumber.isPresent()) {
                        resultGrid[row][column] = minAdjacentNumber.get() + 1;
                    }
                }
            }
        }
        return resultGrid;
    }

    private Integer[][] manhattanDistanceOnABlankGrid(String[][] grid) {
        Integer[][] resultGrid = new Integer[grid.length][grid[0].length];
        for (int times = 0; times < 2500; times++) {
            for (int row = 0; row < grid.length; row++) {
                for (int column = 0; column < grid[row].length; column++) {
                    if (grid[row][column].equals("S")) {
                        resultGrid[row][column] = 0;
                        continue;
                    }
                    List<Integer> surroundings = safeSurroundings(resultGrid, row, column);
                    Optional<Integer> minAdjacentNumber = surroundings.stream().min(Integer::compareTo);
                    if (minAdjacentNumber.isPresent()) {
                        resultGrid[row][column] = minAdjacentNumber.get() + 1;
                    }
                }
            }
        }
        return resultGrid;
    }

    private List<Integer> safeSurroundings(Integer[][] grid, int row, int column) {
        List<Integer> surroundingsList = new ArrayList<>();
        safeGet(grid, row - 1, column).ifPresent(surroundingsList::add);
        safeGet(grid, row, column - 1).ifPresent(surroundingsList::add);
        safeGet(grid, row + 1, column).ifPresent(surroundingsList::add);
        safeGet(grid, row, column + 1).ifPresent(surroundingsList::add);
        return surroundingsList;
    }

    private <T> Optional<T> safeGet(T[][] grid, int row, int column) {
        if (row < 0 || row >= grid.length || column < 0 || column >= grid[0].length) {
            return Optional.empty();
        }
        return Optional.ofNullable(grid[row][column]);
    }
}
