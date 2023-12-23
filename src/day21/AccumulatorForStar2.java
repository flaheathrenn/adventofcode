package day21;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import day18.GridUtils.Direction;

public class AccumulatorForStar2 {

    // State
    List<String[]> gridRows = new ArrayList<>();
    int sRow = -1;
    int sColumn = -1;

    // Update state from parsed line
    public AccumulatorForStar2 update(ParsedLine parsedLine) {
        gridRows.add(parsedLine.row);
        if (parsedLine.sRank != -1) {
            sColumn = parsedLine.sRank;
            sRow = gridRows.size() - 1;
        }
        return this;
    }

    // Extract solution
    public String star2(long steps) {
        final int gridSize = gridRows.size(); // assume square grid

        String[][] grid = gridRows.toArray(new String[gridSize][gridSize]);

        long tXBottomRight = tForDirections(grid, steps, gridSize, Direction.DOWN, Direction.RIGHT);
        long tXBottomLeft = tForDirections(grid, steps, gridSize, Direction.DOWN, Direction.LEFT);
        long tXTopRight = tForDirections(grid, steps, gridSize, Direction.UP, Direction.RIGHT);
        long tXTopLeft = tForDirections(grid, steps, gridSize, Direction.UP, Direction.LEFT);
        long correction = Math.ceilDiv(steps + 1, 2);
        long secondOrderCorrection = (steps + 1) % 2;
        // System.out.println("Bottom right: " + tXBottomRight + "; bottom left: " + tXBottomLeft + "; top right: "
        //         + tXTopRight + "; top left: " + tXTopLeft + "; first order correction: " + correction
        //         + "; second order correction: " + secondOrderCorrection);
        return String.valueOf(
                tXBottomRight + tXBottomLeft + tXTopLeft + tXTopRight - 4 * correction + secondOrderCorrection);
    }

    private long tForDirections(String[][] grid, long steps, int gridSize, Direction firstDirection,
            Direction secondDirection) {
        String[][] shiftedGridS = shiftGrid(grid, firstDirection == Direction.DOWN ? sRow : -sRow,
                secondDirection == Direction.RIGHT ? sColumn : -sColumn);
        Integer[][] resultGrid = manhattanDistance(shiftedGridS);
        int[] mValues = mFinder(resultGrid);

        // print for debugging
        // if (firstDirection == Direction.DOWN && secondDirection == Direction.RIGHT) {
        //     for (int rowIndex = 0; rowIndex < shiftedGridS.length; rowIndex++) {
        //         for (int columnIndex = 0; columnIndex < shiftedGridS[rowIndex].length; columnIndex++) {
        //             if (shiftedGridS[rowIndex][columnIndex].equals("#")) {
        //                 System.out.print("#");
        //             } else if (resultGrid[rowIndex][columnIndex] == null) {
        //                 System.out.print("X"); // unreachable space
        //             } else {
        //                 System.out.print(printable(resultGrid[rowIndex][columnIndex]));
        //             }
        //         }
        //         System.out.println();
        //     }
        //     System.out.println("M values: " + Arrays.stream(mValues).mapToObj(String::valueOf).collect(Collectors.joining(",")));
        // }

        return tX(steps, gridSize, mValues);
    }

    // Extract solution
    public String star2Old() {
        String[][] grid = gridRows.toArray(new String[gridRows.size()][gridRows.get(0).length]);

        String[][] shiftedGridSInTopLeft = shiftGrid(grid, sRow, sColumn);

        Integer[][] resultGrid = manhattanDistance(shiftedGridSInTopLeft);
        int[] mValues = mFinder(resultGrid);
        // print for debugging
        for (int rowIndex = 0; rowIndex < shiftedGridSInTopLeft.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < shiftedGridSInTopLeft[rowIndex].length; columnIndex++) {
                // System.out.print(printable(resultGrid[rowIndex][columnIndex]));
                if (shiftedGridSInTopLeft[rowIndex][columnIndex].equals("#")) {
                    System.out.print("#");
                } else if (resultGrid[rowIndex][columnIndex] == null) {
                    System.out.print("X"); // unreachable space
                } else {
                    System.out.print(printable(resultGrid[rowIndex][columnIndex]));
                }
            }
            System.out.println();
        }

        System.out.println(Arrays.stream(mValues).mapToObj(String::valueOf).collect(Collectors.joining(",")));

        for (int x = 0; x < 10; x++) {
            System.out.println("T" + x + " = " + tX(x, grid.length, mValues));
        }

        return "";
    }

    private long tX(long x, int gridSize, int[] mValues) {
        long tValue = 0;
        for (long termNumber = 1; termNumber < 2 + Math.floorDiv(x, gridSize); termNumber++) {
            long mValueForTerm;
            if (termNumber == 1 + Math.floorDiv(x, gridSize)) { // final term
                mValueForTerm = x % gridSize;
            } else if (x < mValues.length) {
                mValueForTerm = x;
            } else if (termNumber == Math.floorDiv(x, gridSize)) { // penultimate term
                mValueForTerm = (x - gridSize) % (gridSize * 2 - 2);
            } else {
                mValueForTerm = termNumber % 2 == x % 2 ? getIndexOfHighestOddMValue(mValues) : getIndexOfHighestEvenMValue(mValues);
            }
            // System.out.println("Adding term " + termNumber + "M" + (int) mValueForTerm);
            tValue += termNumber * mValues[(int) mValueForTerm];
        }
        return tValue;
    }

    private int getIndexOfHighestOddMValue(int[] mValues) {
        if (mValues.length % 2 == 0) {
            return mValues.length - 1;
        } else {
            return mValues.length - 2;
        }
    }

    private int getIndexOfHighestEvenMValue(int[] mValues) {
        if (mValues.length % 2 == 0) {
            return mValues.length - 2;
        } else {
            return mValues.length - 1;
        }
    }

    private String[][] shiftGrid(String[][] grid, int sRow2, int sColumn2) {
        String[][] resultGrid = new String[grid.length][grid[0].length];
        for (int i = 0; i < resultGrid.length; i++) {
            for (int j = 0; j < resultGrid[i].length; j++) {
                resultGrid[i][j] = grid[Math.floorMod(i + sRow2, grid.length)][Math.floorMod(j + sColumn2,
                        grid[0].length)];
            }
        }
        return resultGrid;
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

    private int[] mFinder(Integer[][] grid) {
        int[] mValues = new int[grid.length * 2 - 1];
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column] != null) {
                    mValues[grid[row][column]]++;
                    for (int x = grid[row][column] + 2; x < mValues.length; x += 2) {
                        mValues[x]++;
                    }
                }
            }
        }
        return mValues;
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
