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
        // System.out.println("Bottom right: " + tXBottomRight + "; bottom left: " +
        // tXBottomLeft + "; top right: "
        // + tXTopRight + "; top left: " + tXTopLeft + "; first order correction: " +
        // correction
        // + "; second order correction: " + secondOrderCorrection);
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
        // for (int rowIndex = 0; rowIndex < shiftedGridS.length; rowIndex++) {
        // for (int columnIndex = 0; columnIndex < shiftedGridS[rowIndex].length;
        // columnIndex++) {
        // if (shiftedGridS[rowIndex][columnIndex].equals("#")) {
        // System.out.print("#");
        // } else if (resultGrid[rowIndex][columnIndex] == null) {
        // System.out.print("X"); // unreachable space
        // } else {
        // System.out.print(printable(resultGrid[rowIndex][columnIndex]));
        // }
        // }
        // System.out.println();
        // }
        // System.out.println("M values: " +
        // Arrays.stream(mValues).mapToObj(String::valueOf).collect(Collectors.joining(",")));
        // }

        long slowValue = tX(steps, gridSize, mValues);
        long fastValue = fastTX(steps, gridSize, mValues);

        if (slowValue != fastValue) {
            System.out.println("Fast/slow mismatch for step count " + steps + ": fast value " + fastValue);
        }

        return slowValue;
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
        // System.out.println("Entered tX: " + System.currentTimeMillis());
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
                mValueForTerm = termNumber % 2 == x % 2 ? getIndexOfHighestOddMValue(mValues)
                        : getIndexOfHighestEvenMValue(mValues);
            }
            // System.out.println("Adding term " + termNumber + "M" + (int) mValueForTerm);
            tValue += termNumber * mValues[(int) mValueForTerm];
        }
        // System.out.println("Leaving tX: " + System.currentTimeMillis());
        return tValue;
    }

    private long fastTX(long x, int gridSize, int[] mValues) {
        // System.out.println("Entered tX: " + System.currentTimeMillis());
        // There will be 1 + floor(x/gridSize) terms.
        // The last and second-to-last terms have to be calculated especially;
        // all the others (that is, terms 1 through floor(x/gridSize) - 1) are simply
        // the term number + either the highest odd or even m value, depending on the
        // parity of the term number and x.
        // So we can work that out using triangle numbers or something.

        long floorOfXOverGridSize = Math.floorDiv(x, gridSize);
        long finalTermCoefficient = 1 + floorOfXOverGridSize;
        int finalTermMValue = (int) (x % gridSize);
        long penultimateTermCoefficient = floorOfXOverGridSize;
        int penultimateTermMValue = 0;
        if (penultimateTermCoefficient > 0) {
            if (x < mValues.length) {
                penultimateTermMValue = (int) x;
            } else {
                penultimateTermMValue = (int) ((x - gridSize) % (gridSize * 2 - 2)); // not entirely sure about this but it
                                                                                 // seems plausible
            }
        }
        long oddTermCoefficientSum = sumOfOddNumbersUpToButNotIncluding(floorOfXOverGridSize);
        int oddTermMValue = x % 2 == 1 ? getIndexOfHighestOddMValue(mValues)
                : getIndexOfHighestEvenMValue(mValues);
        long evenTermCoefficientSum = sumOfEvenNumbersUpToButNotIncluding(floorOfXOverGridSize);
        int evenTermMValue = x % 2 == 1 ? getIndexOfHighestEvenMValue(mValues)
                : getIndexOfHighestOddMValue(mValues);

        System.out.println(String.format("Fast value calculated as %sM%s + %sM%s + %sM%s + %sM%s", oddTermCoefficientSum, oddTermMValue, evenTermCoefficientSum, evenTermMValue, penultimateTermCoefficient, penultimateTermMValue, finalTermCoefficient, finalTermMValue));
        long tValue = (oddTermCoefficientSum * mValues[oddTermMValue])
                + (evenTermCoefficientSum * mValues[evenTermMValue])
                + (penultimateTermCoefficient * mValues[penultimateTermMValue])
                + (finalTermCoefficient * mValues[finalTermMValue]);

        return tValue;
    }

    private long sumOfOddNumbersUpToButNotIncluding(long floorOfXOverGridSize) {
        long acc = 0;
        for (long i = 1; i < floorOfXOverGridSize; i += 2) {
            acc += i;
        }
        return acc;
    }

    private long sumOfEvenNumbersUpToButNotIncluding(long floorOfXOverGridSize) {
        long acc = 0;
        for (long i = 2; i < floorOfXOverGridSize; i += 2) {
            acc += i;
        }
        return acc;
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
        // System.out.println("Entered shiftGrid: " + System.currentTimeMillis());
        String[][] resultGrid = new String[grid.length][grid[0].length];
        for (int i = 0; i < resultGrid.length; i++) {
            for (int j = 0; j < resultGrid[i].length; j++) {
                resultGrid[i][j] = grid[Math.floorMod(i + sRow2, grid.length)][Math.floorMod(j + sColumn2,
                        grid[0].length)];
            }
        }
        // System.out.println("Leaving shiftGrid: " + System.currentTimeMillis());
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
        // System.out.println("Entered manhattanDistance: " +
        // System.currentTimeMillis());
        Integer[][] resultGrid = new Integer[grid.length][grid[0].length];
        for (int times = 0; times < grid.length * 2 + 20; times++) { // should be enough times
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
        // System.out.println("Leaving manhattanDistance: " +
        // System.currentTimeMillis());
        return resultGrid;
    }

    private int[] mFinder(Integer[][] grid) {
        // System.out.println("Entered mFinder: " + System.currentTimeMillis());
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
        // System.out.println("Leaving mFinder: " + System.currentTimeMillis());
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
