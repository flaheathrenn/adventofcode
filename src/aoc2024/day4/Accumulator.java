package aoc2024.day4;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

public class Accumulator {
    // State
    int currentRow = 0;
    Set<GridCoordinate> xPositions = new HashSet<>();
    Set<GridCoordinate> mPositions = new HashSet<>();
    Set<GridCoordinate> aPositions = new HashSet<>();
    Set<GridCoordinate> sPositions = new HashSet<>();
    // I think I wrote it this way assuming that these letters would be relatively sparse in
    // the input grid, rather than comprising the entire grid between them


    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        for (int i = 0; i < parsedLine.row.length; i++) {
            (switch (parsedLine.row[i]) {
                case "X": yield xPositions;
                case "M": yield mPositions;
                case "A": yield aPositions;
                case "S": yield sPositions;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + parsedLine.row[i]); 
            }).add(new GridCoordinate(currentRow, i));
        }
        currentRow++;
        return this;
    }

    // Extract solution
    public String star1() {
        long xmasCount = 0;

        List<BiFunction<GridCoordinate, Integer, GridCoordinate>> directionalFunctions = List.of(
            GridCoordinate::up,
            GridCoordinate::down,
            GridCoordinate::left,
            GridCoordinate::right,
            GridCoordinate::upleft,
            GridCoordinate::downleft,
            GridCoordinate::upright,
            GridCoordinate::downright
        );

        for (GridCoordinate xPos : xPositions) {
            for (BiFunction<GridCoordinate, Integer, GridCoordinate> directionalFunction : directionalFunctions) {
                if (checkX(xPos, directionalFunction)) {
                    xmasCount++;
                }
            }
        }

        return String.valueOf(xmasCount);
    }

    public String star2() {
        long xmasCount = 0;

        for (GridCoordinate aPos : aPositions) {
            if (mPositions.contains(aPos.upleft(1)) && mPositions.contains(aPos.upright(1)) && sPositions.contains(aPos.downleft(1)) && sPositions.contains(aPos.downright(1))) {
                xmasCount++;
            }
            if (mPositions.contains(aPos.upright(1)) && mPositions.contains(aPos.downright(1)) && sPositions.contains(aPos.downleft(1)) && sPositions.contains(aPos.upleft(1))) {
                xmasCount++;
            }
            if (mPositions.contains(aPos.downright(1)) && mPositions.contains(aPos.downleft(1)) && sPositions.contains(aPos.upleft(1)) && sPositions.contains(aPos.upright(1))) {
                xmasCount++;
            }
            if (mPositions.contains(aPos.downleft(1)) && mPositions.contains(aPos.upleft(1)) && sPositions.contains(aPos.upright(1)) && sPositions.contains(aPos.downright(1))) {
                xmasCount++;
            }
        }

        return String.valueOf(xmasCount);
    }

    public boolean checkX(GridCoordinate xCoordinate, BiFunction<GridCoordinate, Integer, GridCoordinate> directionalFunction) {
        return mPositions.contains(directionalFunction.apply(xCoordinate, 1))
            && aPositions.contains(directionalFunction.apply(xCoordinate, 2))
            && sPositions.contains(directionalFunction.apply(xCoordinate, 3));
    }

    public static record GridCoordinate(int row, int column) {
        public GridCoordinate up(int spaces) {
            return new GridCoordinate(row - spaces, column);
        }
        public GridCoordinate down(int spaces) {
            return new GridCoordinate(row + spaces, column);
        }
        public GridCoordinate left(int spaces) {
            return new GridCoordinate(row, column - spaces);
        }
        public GridCoordinate right(int spaces) {
            return new GridCoordinate(row, column + spaces);
        }
        public GridCoordinate upleft(int spaces) {
            return new GridCoordinate(row - spaces, column - spaces);
        }
        public GridCoordinate upright(int spaces) {
            return new GridCoordinate(row - spaces, column + spaces);
        }
        public GridCoordinate downleft(int spaces) {
            return new GridCoordinate(row + spaces, column - spaces);
        }
        public GridCoordinate downright(int spaces) {
            return new GridCoordinate(row + spaces, column + spaces);
        }
    };
}
